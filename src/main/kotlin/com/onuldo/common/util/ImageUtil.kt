package com.onuldo.common.util

import org.springframework.web.multipart.MultipartFile
import java.awt.Graphics2D
import java.awt.Image
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.imageio.ImageIO
import javax.imageio.ImageWriter
import javax.imageio.ImageWriteParam
import javax.imageio.stream.ImageOutputStream

object ImageUtil {
    private const val MAX_WIDTH = 1920
    private const val MAX_HEIGHT = 1920
    private const val THUMBNAIL_WIDTH = 300
    private const val THUMBNAIL_HEIGHT = 300

    init {
        // TwelveMonkeys ImageIO는 자동으로 WebP 지원을 등록합니다
        // 별도의 초기화가 필요하지 않습니다
    }

    /**
     * 이미지를 WebP 형식으로 리사이징
     */
    fun resizeImageToWebP(inputStream: InputStream, maxWidth: Int, maxHeight: Int): ByteArray {
        val originalImage = ImageIO.read(inputStream)
            ?: throw IllegalArgumentException("이미지 파일을 읽을 수 없습니다.")

        val (newWidth, newHeight) = calculateDimensions(
            originalImage.width,
            originalImage.height,
            maxWidth,
            maxHeight
        )

        val resizedImage = BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB)
        val graphics: Graphics2D = resizedImage.createGraphics()
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        graphics.drawImage(originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null)
        graphics.dispose()

        // WebP로 변환
        return convertToWebP(resizedImage)
    }

    /**
     * BufferedImage를 WebP 형식으로 변환
     */
    private fun convertToWebP(image: BufferedImage): ByteArray {
        val outputStream = ByteArrayOutputStream()
        
        try {
            // TwelveMonkeys ImageIO를 사용하여 WebP로 저장
            val webpWriters = ImageIO.getImageWritersByFormatName("webp")
            if (webpWriters.hasNext()) {
                val writer = webpWriters.next()
                val imageOutputStream = ImageIO.createImageOutputStream(outputStream)
                writer.output = imageOutputStream
                
                val writeParam = writer.defaultWriteParam
                if (writeParam.canWriteCompressed()) {
                    writeParam.compressionMode = ImageWriteParam.MODE_EXPLICIT
                    writeParam.compressionQuality = 0.85f // 85% 품질 (WebP 최적화)
                }
                
                writer.write(null, javax.imageio.IIOImage(image, null, null), writeParam)
                writer.dispose()
                imageOutputStream.close()
            } else {
                // WebP writer가 없으면 PNG로 저장 (fallback)
                ImageIO.write(image, "png", outputStream)
            }
        } catch (e: Exception) {
            // WebP 변환 실패 시 PNG로 저장 (fallback)
            ImageIO.write(image, "png", outputStream)
        }
        
        return outputStream.toByteArray()
    }

    /**
     * 썸네일을 WebP 형식으로 생성
     */
    fun createThumbnailToWebP(inputStream: InputStream): ByteArray {
        return resizeImageToWebP(inputStream, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT)
    }

    /**
     * 이미지 크기 계산
     */
    private fun calculateDimensions(originalWidth: Int, originalHeight: Int, maxWidth: Int, maxHeight: Int): Pair<Int, Int> {
        val widthRatio = maxWidth.toDouble() / originalWidth
        val heightRatio = maxHeight.toDouble() / originalHeight
        val ratio = minOf(widthRatio, heightRatio, 1.0)

        return Pair(
            (originalWidth * ratio).toInt(),
            (originalHeight * ratio).toInt()
        )
    }

    /**
     * 이미지 크기 가져오기
     */
    fun getImageDimensions(inputStream: InputStream): Pair<Int, Int> {
        val image = ImageIO.read(inputStream)
            ?: throw IllegalArgumentException("이미지 파일을 읽을 수 없습니다.")
        return Pair(image.width, image.height)
    }

    /**
     * 이미지 파일 검증
     */
    fun validateImageFile(file: MultipartFile): Boolean {
        if (file.isEmpty) return false

        val contentType = file.contentType ?: return false
        if (!contentType.startsWith("image/")) return false

        val allowedTypes = listOf("image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp")
        return allowedTypes.contains(contentType.lowercase())
    }

    /**
     * 파일 크기 검증 (10MB 제한)
     */
    fun validateFileSize(file: MultipartFile, maxSizeBytes: Long = 10 * 1024 * 1024): Boolean {
        return file.size <= maxSizeBytes
    }
}


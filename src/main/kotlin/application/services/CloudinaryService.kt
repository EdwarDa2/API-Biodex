package com.Biodex.application.services

import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import java.io.File

class CloudinaryService {


    private val cloudinary = Cloudinary(ObjectUtils.asMap(
        "cloud_name", "dawywr3zf",
        "api_key", "972539569847826",
        "api_secret", "FzFov-ljPNweftPpoPoQQS_4f4k",
        "secure", true
    ))

    fun uploadFile(fileBytes: ByteArray, fileName: String, folderName: String): String {
        val params = ObjectUtils.asMap(
            "public_id", fileName.substringBeforeLast("."),
            "folder", folderName, // <--- Aquí usamos la variable en vez del texto fijo
            "resource_type", "auto"
        )

        val uploadResult = cloudinary.uploader().upload(fileBytes, params)

        return uploadResult["secure_url"].toString()
    }
}
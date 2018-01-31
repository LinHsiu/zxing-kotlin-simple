package com.showlin.zxing_kotlin_simple

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.common.CharacterSetECI
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.util.*


object Utility {
    val TYPE_TEXT = -1
    val TYPE_WIFI = 0
    val TYPE_URL = 1
    val TYPE_SMS = 2
    val TYPE_GEO = 3
    val TYPE_TEL = 4
    val TYPE_MAIL = 5
    val TYPE_MECARD = 6
    val TYPE_VCARD = 7
    val TYPE_VEVENT = 8

    var type = -1

    fun scan(activity: Activity){
        val integrator = IntentIntegrator(activity)
        integrator.captureActivity = ScanActivity::class.java
        integrator.setPrompt("")
        integrator.setBeepEnabled(false)
        integrator.initiateScan()
    }

    @Throws(WriterException::class)
    fun generateQR(value: String) : Bitmap? {
        val bitMatrix: BitMatrix
        try {
            val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H)
            hints.put(EncodeHintType.CHARACTER_SET, CharacterSetECI.UTF8)

            bitMatrix = MultiFormatWriter().encode(
                    value,
                    BarcodeFormat.QR_CODE,
                    500,
                    500,
                    hints
            )
        }catch (Illegalargumentexception : IllegalArgumentException){
            return null
        }

        val bitMatrixWidth = bitMatrix.width

        val bitMatrixHeight = bitMatrix.height

        val pixels = IntArray(bitMatrixWidth * bitMatrixHeight)

        for (y in 0 until bitMatrixHeight) {
            val offset = y * bitMatrixWidth

            for (x in 0 until bitMatrixWidth) {

                pixels[offset + x] = if (bitMatrix.get(x, y))
                    Color.BLACK
                else
                    Color.WHITE
            }
        }
        val bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444)
        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight)
        return bitmap
    }

    fun pareInfo(resultString: String):String{
        var result = ""
        if (resultString.indexOf("WIFI:") == 0){
            type = TYPE_WIFI
            result = "WIFI"
            return String.format("Type: $result \nResult:\n$resultString")
        }else if (resultString.indexOf("http") == 0){
            type = TYPE_URL
            result = "URL"
            return String.format("Type: $result \nResult:\n$resultString")
        }else if (resultString.indexOf("smsto:") == 0){
            type = TYPE_SMS
            result = "SMS"
            return String.format("Type: $result \nResult:\n$resultString")
        }else if (resultString.indexOf("geo:") == 0){
            type = TYPE_GEO
            result = "GEO"
            return String.format("Type: $result \nResult:\n$resultString")
        }else if (resultString.indexOf("tel:") == 0){
            type = TYPE_TEL
            result = "TEL"
            return String.format("Type: $result \nResult:\n$resultString")
        }else if (resultString.indexOf("mailto:") == 0){
            type = TYPE_MAIL
            result = "E-MAIL"
            return String.format("Type: $result \nResult:\n$resultString")
        }else if (resultString.indexOf("MECARD:") == 0){
            type = TYPE_MECARD
            result = "CONTACT"
            return String.format("Type: $result \nResult:\n$resultString")
        }else if (resultString.indexOf("BEGIN:VCARD") == 0){
            type = TYPE_VCARD
            result = "CONTACT"
            return String.format("Type: $result \nResult:\n$resultString")
        }else if (resultString.indexOf("BEGIN:VEVENT") == 0){
            type = TYPE_VEVENT
            result = "Calendar"
            return String.format("Type: $result \nResult:\n$resultString")
        }else{
            type = TYPE_TEXT
            result = "TEXT"
            return String.format("Type: $result \nResult:\n$resultString")
        }
    }
}
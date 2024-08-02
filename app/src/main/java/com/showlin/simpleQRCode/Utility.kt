package com.showlin.simpleQRCode

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.common.CharacterSetECI
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.showlin.simpleQRCode.activity.ScanActivity
import java.util.*


object Utility {
    const val KEY_RESULT = "KEY_RESULT"
    const val KEY_CONTENT = "KEY_CONTENT"

    const val REQUEST_CODE= 2304
    const val WRITE_PERMISSION = 2134

    fun scan(activity: Activity){
        val intent = Intent(activity, ScanActivity::class.java)
        activity.startActivityForResult(intent, REQUEST_CODE)
    }

    @Throws(WriterException::class)
    fun generateQR(value: String) : Bitmap? {
        val bitMatrix: BitMatrix
        try {
            val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
            hints[EncodeHintType.CHARACTER_SET] = CharacterSetECI.UTF8

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
        bitmap.setPixels(pixels, 0, bitMatrixWidth, 0, 0, bitMatrixWidth, bitMatrixHeight)

        return bitmap
    }
}

package com.showlin.simpleQRCode.activity

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.view.MenuItem
import android.view.View
import com.showlin.simpleQRCode.R
import com.showlin.simpleQRCode.Utility
import com.showlin.simpleQRCode.Utility.KEY_CONTENT
import com.showlin.simpleQRCode.Utility.REQUEST_CODE
import kotlinx.android.synthetic.main.activity_generate.*
import java.io.IOException
import java.util.*


class GenerateActivity : BaseActivity(){
    private var mTextInput : String? = null
    private var mQRBitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate)

        supportActionBar!!.title = getString(R.string.generate)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        btn_go_generate.setOnClickListener {
            mQRBitmap = Utility.generateQR(edit_input.text.toString())
            if (mQRBitmap != null){
                mTextInput = edit_input.text.toString()
                img_qr_code.setImageBitmap(mQRBitmap)
                btn_save.visibility = View.VISIBLE
            }else{
                btn_save.visibility = View.INVISIBLE
                img_qr_code.setImageBitmap(null)
                mQRBitmap = null
            }
        }

        btn_save.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED){
                    val strings = Array<String>(1) { "android.permission.WRITE_EXTERNAL_STORAGE" }
                    requestPermissions(strings, Utility.WRITE_PERMISSION)
                    return@setOnClickListener
                }
            }

            CapturePhotoUtils.insertImage(contentResolver, mQRBitmap, Date().time.toString(), this.mTextInput!!)
        }

        btn_type_generate.setOnClickListener {
            val intent = Intent(this@GenerateActivity, TypeGenerateActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE){
            if (data != null){
                val strContent = data!!.getStringExtra(KEY_CONTENT)
                edit_input.setText(strContent)
                mQRBitmap = Utility.generateQR(edit_input.text.toString())
                if (mQRBitmap != null){
                    mTextInput = edit_input.text.toString()
                    img_qr_code.setImageBitmap(mQRBitmap)
                    btn_save.visibility = View.VISIBLE
                }else{
                    btn_save.visibility = View.INVISIBLE
                    img_qr_code.setImageBitmap(null)
                    mQRBitmap = null
                }
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Utility.WRITE_PERMISSION){
            for (i in permissions.indices ){
                if (grantResults.get(i) !=  PackageManager.PERMISSION_GRANTED){
                    val strings = Array<String>(1) { "android.permission.WRITE_EXTERNAL_STORAGE" }
                    requestPermissions(strings, Utility.WRITE_PERMISSION)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}


object CapturePhotoUtils {

    fun insertImage(cr: ContentResolver,
                    source: Bitmap?,
                    title: String,
                    description: String): String? {

        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, title)
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title)
        values.put(MediaStore.Images.Media.DESCRIPTION, description)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        // Add the date meta data to ensure the image is added at the front of the gallery
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())

        var url: Uri? = null
        var stringUrl: String? = null    /* value to be returned */

        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

            if (source != null) {
                assert(url != null)
                val imageOut = cr.openOutputStream(url!!)
                try {
                    source.compress(Bitmap.CompressFormat.JPEG, 50, imageOut)
                } finally {
                    assert(imageOut != null)
                    imageOut!!.close()
                }

                val id = ContentUris.parseId(url)
                // Wait until MINI_KIND thumbnail is generated.
                val miniThumb = MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MINI_KIND, null)
                // This is for backward compatibility.
                storeThumbnail(cr, miniThumb, id, MediaStore.Images.Thumbnails.MICRO_KIND)
            } else {
                assert(url != null)
                cr.delete(url!!, null, null)
                url = null
            }
        } catch (e: Exception) {
            if (url != null) {
                cr.delete(url, null, null)
                url = null
            }
        }

        if (url != null) {
            stringUrl = url.toString()
        }

        return stringUrl
    }

    private fun storeThumbnail(
            cr: ContentResolver,
            source: Bitmap,
            id: Long,
            kind: Int) {

        val matrix = Matrix()

        val scaleX = 50f / source.width
        val scaleY = 50f / source.height

        matrix.setScale(scaleX, scaleY)

        val thumb = Bitmap.createBitmap(source, 0, 0,
                source.width,
                source.height, matrix,
                true
        )

        val values = ContentValues(4)
        values.put(MediaStore.Images.Thumbnails.KIND, kind)
        values.put(MediaStore.Images.Thumbnails.IMAGE_ID, id.toInt())
        values.put(MediaStore.Images.Thumbnails.HEIGHT, thumb.height)
        values.put(MediaStore.Images.Thumbnails.WIDTH, thumb.width)

        val url = cr.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, values)

        try {
            assert(url != null)
            val thumbOut = cr.openOutputStream(url!!)
            thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut)
            assert(thumbOut != null)
            thumbOut!!.close()
        } catch (ignored: IOException) {
        }

    }
}
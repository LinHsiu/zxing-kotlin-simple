package com.showlin.simpleQRCode.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.showlin.simpleQRCode.R
import com.showlin.simpleQRCode.Utility.KEY_CONTENT
import com.showlin.simpleQRCode.Utility.REQUEST_CODE
import kotlinx.android.synthetic.main.activity_type_generate.*
import kotlinx.android.synthetic.main.dialog_geo.*
import kotlinx.android.synthetic.main.dialog_mail.*
import kotlinx.android.synthetic.main.dialog_sms.*
import kotlinx.android.synthetic.main.dialog_tel.*
import kotlinx.android.synthetic.main.dialog_web.*
import kotlinx.android.synthetic.main.dialog_wifi.*


class TypeGenerateActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type_generate)

        supportActionBar!!.title = getString(R.string.type_generate)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        recycle_view.setHasFixedSize(true)
        recycle_view.layoutManager = GridLayoutManager(this, 3)
        recycle_view.adapter = TypeAdapter(this)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    fun sendResult(strResult : String){
        val intent = Intent()
        intent.putExtra(KEY_CONTENT, strResult)
        setResult(REQUEST_CODE, intent)
    }
}

class TypeAdapter(private val activity: TypeGenerateActivity) : RecyclerView.Adapter<TypeHolder>() {

    private val mTypeNameArray = intArrayOf(R.string.wifi, R.string.website, R.string.sms, R.string.geo, R.string.tel, R.string.mail, R.string.mecard, R.string.vcard, R.string.vevent)
    private val mTypeArray = intArrayOf(R.drawable.img_wifi, R.drawable.img_url, R.drawable.img_sms, R.drawable.img_geo, R.drawable.img_tel, R.drawable.img_mail, R.drawable.img_contactor, R.drawable.img_contactor, R.drawable.img_calendar)
    private val mTypePressArray = intArrayOf(R.drawable.img_wifi_press, R.drawable.img_url_press, R.drawable.img_sms_press, R.drawable.img_geo_press, R.drawable.img_tel_press, R.drawable.img_mail_press, R.drawable.img_contactor_press, R.drawable.img_contactor_press, R.drawable.img_calendar_press)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_type, parent, false)

        return TypeHolder(view)
    }

    override fun onBindViewHolder(holder: TypeHolder, position: Int) {
        setClickStatusImg(activity, mTypeArray[position], mTypePressArray[position], holder.imgLogo)
        setClickStatusTxt(activity, R.color.colorWhite, R.color.color2, holder.textTypeName)
        holder.textTypeName.setText(mTypeNameArray[position])


        holder.llGroup.setOnClickListener {
            when (position) {
                TYPE_WIFI -> {
                    val dialog = Dialog(activity)
                    dialog.setContentView(R.layout.dialog_wifi)
                    dialog.setCancelable(false)
                    dialog.group_radio.check(dialog.type_none.id)
                    dialog.show()
                    dialog.text_dialog_generate.setOnClickListener {
                        val strWifi = generateWifiString(dialog.edit_ssid.text.toString(), dialog.edit_password.text.toString(), dialog.group_radio.checkedRadioButtonId, dialog.check_box.isChecked, dialog)
                        activity.sendResult(strWifi)
                        activity.finish()
                        dialog.dismiss()
                    }
                    dialog.text_dialog_cancel.setOnClickListener {
                        dialog.dismiss()
                    }
                }
                TYPE_WEBSITE -> {
                    val dialog = Dialog(activity)
                    dialog.setCancelable(false)
                    dialog.setContentView(R.layout.dialog_web)
                    dialog.show()
                    dialog.text_dialog_generate_url.setOnClickListener {
                        val strURL = generateURLString(dialog.edit_url.text.toString())
                        activity.sendResult(strURL)
                        activity.finish()
                        dialog.dismiss()
                    }
                    dialog.text_dialog_cancel_url.setOnClickListener {
                        dialog.dismiss()
                    }
                }
                TYPE_SMS -> {
                    val dialog = Dialog(activity)
                    dialog.setCancelable(false)
                    dialog.setContentView(R.layout.dialog_sms)
                    dialog.show()
                    dialog.text_dialog_generate_sms.setOnClickListener {
                        val strURL = generateSMSString(dialog.edit_sms_tel.text.toString(), dialog.edit_sms_message.text.toString())
                        activity.sendResult(strURL)
                        activity.finish()
                        dialog.dismiss()
                    }
                    dialog.text_dialog_cancel_sms.setOnClickListener {
                        dialog.dismiss()
                    }
                }
                TYPE_GEO -> {
                    val dialog = Dialog(activity)
                    dialog.setCancelable(false)
                    dialog.setContentView(R.layout.dialog_geo)
                    dialog.show()
                    dialog.text_dialog_generate_geo.setOnClickListener {
                        val strURL = generateGEOString(dialog.edit_geo_latitude.text.toString(), dialog.edit_geo_longitude.text.toString(), dialog.edit_geo_query.text.toString())
                        activity.sendResult(strURL)
                        activity.finish()
                        dialog.dismiss()
                    }
                    dialog.text_dialog_cancel_geo.setOnClickListener {
                        dialog.dismiss()
                    }
                }
                TYPE_TEL -> {
                    val dialog = Dialog(activity)
                    dialog.setCancelable(false)
                    dialog.setContentView(R.layout.dialog_tel)
                    dialog.show()
                    dialog.text_dialog_generate_tel.setOnClickListener {
                        val strURL = generateTELString(dialog.edit_tel_number.text.toString())
                        activity.sendResult(strURL)
                        activity.finish()
                        dialog.dismiss()
                    }
                    dialog.text_dialog_cancel_tel.setOnClickListener {
                        dialog.dismiss()
                    }
                }
                TYPE_MAIL -> {
                    val dialog = Dialog(activity)
                    dialog.setCancelable(false)
                    dialog.setContentView(R.layout.dialog_mail)
                    dialog.show()
                    dialog.text_dialog_generate_mail.setOnClickListener {
                        val strURL = generateMAILString(dialog.edit_mail_address.text.toString())
                        activity.sendResult(strURL)
                        activity.finish()
                        dialog.dismiss()
                    }
                    dialog.text_dialog_cancel_mail.setOnClickListener {
                        dialog.dismiss()
                    }
                }
                TYPE_MECARD -> {
                }
                TYPE_VCARD -> {
                }
                TYPE_VEVENT -> {
                }
            }
        }

        holder.textTypeName.setOnClickListener { }
    }

    private fun generateMAILString(mail: String): String {
        var strResult = "mailto:"
        strResult += mail
        return strResult
    }

    private fun generateTELString(tel: String): String {
        var strResult = "tel:"
        strResult += tel
        return strResult
    }

    private fun generateGEOString(latitude: String, longitude: String, query: String): String {
        var strResult = "geo:"
        strResult += latitude + "," + longitude + "q=" + query
        return strResult
    }

    private fun generateSMSString(tel: String, message: String): String {
        var strResult = "smsto:"
        if (tel == ""){
            return ""
        }
        strResult += tel
        strResult += ":" + message
        return strResult
    }

    private fun generateURLString(url: String): String {
        var strResult = url
        if (url.indexOf("http") != 0){
            strResult = "http://" + url
        }
        return strResult
    }

    private fun generateWifiString(ssid: String, password: String, type: Int, hidden: Boolean, dialog: Dialog): String {
        if (ssid == ""){
            return ""
        }
        var strResult = "WIFI:"
        strResult += "S:$ssid;"
        if (password != ""){
            strResult += "P:$password;"
        }
        when(type){
            dialog.type_wep.id->{
                strResult += "T:WEP;"
            }
            dialog.type_wpa.id->{
                strResult += "T:WPA;"
            }
            dialog.type_none.id->{}
        }

        if (hidden){
            strResult += "H:true;"
        }
        strResult += ";"
        return strResult
    }



    override fun getItemCount(): Int {
        return mTypeNameArray.size - 3
    }

    private fun setClickStatusImg(context: Context, nNormal: Int, nPressed: Int, imgview: ImageView) {
        val states = StateListDrawable()
        states.addState(intArrayOf(android.R.attr.state_pressed), ContextCompat.getDrawable(context, nPressed))
        states.addState(intArrayOf(android.R.attr.state_focused), ContextCompat.getDrawable(context, nPressed))
        states.addState(intArrayOf(), ContextCompat.getDrawable(context, nNormal))
        imgview.setImageDrawable(states)
    }

    private fun setClickStatusTxt(context: Context, nNormal: Int, nPressed: Int, textView: TextView) {
        val colorStateList = ColorStateList(
                arrayOf(intArrayOf(android.R.attr.state_pressed), intArrayOf(android.R.attr.state_selected), intArrayOf(-android.R.attr.state_selected)),
                intArrayOf(ContextCompat.getColor(context, nPressed), ContextCompat.getColor(context, nPressed), ContextCompat.getColor(context, nNormal)))
        textView.setTextColor(colorStateList)
    }

    companion object {
        const val TYPE_WIFI = 0
        const val TYPE_WEBSITE = 1
        const val TYPE_SMS = 2
        const val TYPE_GEO = 3
        const val TYPE_TEL = 4
        const val TYPE_MAIL = 5
        const val TYPE_MECARD = 6
        const val TYPE_VCARD = 7
        const val TYPE_VEVENT = 8
    }
}

class TypeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var llGroup: LinearLayout = itemView.findViewById(R.id.ll_group)
    var imgLogo: ImageView = itemView.findViewById(R.id.img_logo)
    var textTypeName: TextView = itemView.findViewById(R.id.text_type_name)

}


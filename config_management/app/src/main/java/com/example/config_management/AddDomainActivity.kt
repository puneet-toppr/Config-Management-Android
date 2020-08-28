package com.example.config_management

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.BaseAdapter
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_edit_domain.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddDomainActivity : AppCompatActivity() {

    private val api = ApiInterface.getClient().create(ApiService::class.java)
    private var listFeatureInfo : List<DomainFeatureModel> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_edit_domain)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbartext.text = "Add Domain"
        editdomainbuttonfeatures.text = "Add Mapping"

        val callNew = api.fetchAllFeatures()
        onFetchFeatures(callNew)

        editdomainbuttonfeatures.setOnClickListener {
            acceptList()
        }

        buttonSubmiteditDomain.setOnClickListener {
            val name = editdomainname.text.toString().trim()

            if (name.isEmpty()) {
                editdomainname.error = "Name required!"
                editdomainname.requestFocus()
                return@setOnClickListener
            }
            val feature_id_list = arrayListOf<Int>()
            for (i in listFeatureInfo.indices) {
                if (listFeatureInfo[i].isSelected) {
                    feature_id_list.add(listFeatureInfo[i].id)
                }

            }

            val postData = AddDomain(name, feature_id_list)

            api.addDomain(postData)
                .enqueue(object : Callback<AddDomainResponse> {
                    override fun onResponse(
                        call: Call<AddDomainResponse>,
                        response: Response<AddDomainResponse>
                    ) {
                        Log.e("didnot work", "onResponse: " + response.body()!!.errorMessage)
                        if (response.body()?.errorMessage != null) {
                            Toast.makeText(
                                applicationContext,
                                response.body()!!.errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Domain " + response.body()!!.domainInfo.name + " has been created",
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<AddDomainResponse>, t: Throwable) {
                        Log.e("didnot work", "onResponse: " + t.message)
                    }

                })
        }
        buttonCanceleditDomain.setOnClickListener {
            finish()
        }
    }

    fun onFetchFeatures(call: Call<FeaturesInfo>) {
        call.enqueue(object : Callback<FeaturesInfo> {
            override fun onResponse(
                call: Call<FeaturesInfo>,
                response: Response<FeaturesInfo>
            ) {

                response.body()?.featuresInfo?.let {
                    listFeatureInfo = it
                }
            }
            override fun onFailure(call: Call<FeaturesInfo>, t: Throwable) {
                Log.e("fail", "no_resp" + t.message)
            }

        })
    }

    private fun acceptList() {

        var temp = BooleanArray(listFeatureInfo.size)
        val mainList = Array(listFeatureInfo.size){ "" }

        for (i in listFeatureInfo.indices){
            mainList[i] = listFeatureInfo[i].name
            temp[i] = listFeatureInfo[i].isSelected
        }

        val builder = AlertDialog.Builder(this)

        builder.setTitle("Checked Feature List")
        builder.setCancelable(false)

        builder.setMultiChoiceItems(mainList, temp) { dialog, pos, isChecked ->
            temp[pos] = isChecked
        }

        builder.setPositiveButton("Submit") { dialog, _ ->
            for (i in temp.indices){
                listFeatureInfo[i].isSelected = temp[i]
            }
            dialog.dismiss()
        }

        builder.setNeutralButton("Clear Selection") { dialog, _ ->
            for (i in listFeatureInfo.indices){
                listFeatureInfo[i].isSelected = false
            }
            Toast.makeText(
                this@AddDomainActivity,
                "All Feature Selections have been cleared",
                Toast.LENGTH_LONG
            ).show()
            dialog.dismiss()
            acceptList()
        }

        builder.setNegativeButton("Cancel"){dialog, _ ->
            dialog.dismiss()
        }

        var dialog = builder.create()
        dialog.show()

    }


}
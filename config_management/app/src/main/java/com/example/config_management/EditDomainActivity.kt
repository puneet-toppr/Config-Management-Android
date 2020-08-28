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

class EditDomainActivity : AppCompatActivity() {

    private var domain_name: String? = null
    private val api = ApiInterface.getClient().create(ApiService::class.java)
    private var listFeatureInfo : List<DomainFeatureModel> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_edit_domain)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        var idextras = intent.getIntExtra("id", -1)

        editdomainbuttonfeatures.setOnClickListener {
            acceptList()
        }

        val call = api.detailDomain(idextras)
        onDetailDomain(call)

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

            val posteditData = EditDomain(idextras, name, feature_id_list)
            val api = ApiInterface.getClient().create(ApiService::class.java)

            api.editDomain(idextras, posteditData)
                .enqueue(object : Callback<EditDomainResponse> {
                    override fun onResponse(
                        call: Call<EditDomainResponse>,
                        response: Response<EditDomainResponse>
                    ) {
                        Log.e("didnot work", "onResponse: " + response.body()!!.errorMessage)
                        if (response.body()?.errorMessage != null) {
                            Toast.makeText(
                                applicationContext,
                                response.body()?.errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Domain " + editdomainname.text + " has been edited",
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        }
                    }
                    override fun onFailure(call: Call<EditDomainResponse>, t: Throwable) {
                        Log.e("didnot work", "onResponse: " + t.message)
                    }

                })
        }
        buttonCanceleditDomain.setOnClickListener {
            finish()
        }
    }

    fun onFetchFeatures(call: Call<FeaturesInfo>, list: List<DomainFeatureModel>) {
        call.enqueue(object : Callback<FeaturesInfo> {
            override fun onResponse(
                call: Call<FeaturesInfo>,
                response: Response<FeaturesInfo>
            ) {

                    response.body()?.featuresInfo?.let {
                        listFeatureInfo = it
                    for (i in listFeatureInfo.indices) {
//                            featureNames.add(res[i].name)
//                            featureIds.add(res[i].id)
                        for (j in list.indices) {
                            if (listFeatureInfo[i].id == list[j].id) {
                                listFeatureInfo[i].isSelected = true
                            }
                        }
                    }
                }
            }
            override fun onFailure(call: Call<FeaturesInfo>, t: Throwable) {
                Log.e("fail", "no_resp" + t.message)
            }

        })
    }

    fun onDetailDomain(call: Call<DomainDetail>) {
        call.enqueue(object : Callback<DomainDetail> {
            override fun onResponse(
                call: Call<DomainDetail>,
                response: Response<DomainDetail>
            ) {
                if (response.isSuccessful) {
                    editdomainname.setText(response.body()?.domainInfo?.name)
                    domain_name = response.body()!!.domainInfo.name

                    response.body()?.domainInfo?.featureList?.let {
                        val callNew = api.fetchAllFeatures()
                        onFetchFeatures(callNew, it)
                    }

                } else {
                    Log.e("not working", "onResponse: ")
                }
            }

            override fun onFailure(call: Call<DomainDetail>, t: Throwable) {
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
                this@EditDomainActivity,
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
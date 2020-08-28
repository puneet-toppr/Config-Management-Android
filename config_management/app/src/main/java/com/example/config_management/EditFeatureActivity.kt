package com.example.config_management

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_edit_feature.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditFeatureActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_edit_feature)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        var idextras = intent.getIntExtra("id", -1)

        val api = ApiInterface.getClient().create(ApiService::class.java)

        fun onDetailFeature(call: Call<FeatureDetail>) {
            call.enqueue(object : Callback<FeatureDetail> {
                override fun onResponse(call: Call<FeatureDetail>, response: Response<FeatureDetail>) {
                    if (response.isSuccessful) {
                        editfeaturename.setText(response.body()?.featureInfo?.name)
                    } else {
                        Log.e("not working", "onResponse: ")
                    }
                }

                override fun onFailure(call: Call<FeatureDetail>, t: Throwable) {
                    Log.e("fail", "no_resp" + t.message)
                }

            })
        }

        val call = api.detailFeature(idextras)
        onDetailFeature(call)

        buttonSubmitEditFeature.setOnClickListener{


            val name = editfeaturename.text.toString()

            if (name.isEmpty()){
                editfeaturename.error = "Name required!"
                editfeaturename.requestFocus()
                return@setOnClickListener
            }

            val postEditData = EditFeature(idextras, name)

            api.editFeature(idextras, postEditData)
                .enqueue(object : Callback<EditFeatureResponse>{
                    override fun onResponse(
                        call: Call<EditFeatureResponse>,
                        response: Response<EditFeatureResponse>
                    ) {
                        Log.e("didnot work", "onResponse: " + response.body()?.errorMessage)
                        if (response.body()?.errorMessage!=null){
                            Toast.makeText(applicationContext, response.body()?.errorMessage, Toast.LENGTH_LONG).show()
                        }
                        else{
                            Toast.makeText(applicationContext, "Feature "+response.body()?.featureInfo?.name+" has been edited", Toast.LENGTH_LONG).show()
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<EditFeatureResponse>, t: Throwable) {
                        Log.e("didnot work", "onResponse: " + t.message)
                    }

                })

        }

        buttonCancelEditFeature.setOnClickListener {
            finish()
        }

    }

}
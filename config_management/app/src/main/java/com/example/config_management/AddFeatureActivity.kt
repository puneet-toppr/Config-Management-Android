package com.example.config_management

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_edit_domain.*
import kotlinx.android.synthetic.main.fragment_edit_feature.*
import kotlinx.android.synthetic.main.fragment_edit_feature.toolbar
import kotlinx.android.synthetic.main.fragment_edit_feature.toolbartext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddFeatureActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_edit_feature)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val api = ApiInterface.getClient().create(ApiService::class.java)

        toolbartext.text = "Add Feature"

        buttonSubmitEditFeature.setOnClickListener{


            val name = editfeaturename.text.toString().trim()

            if (name.isEmpty()){
                editfeaturename.error = "Name required!"
                editfeaturename.requestFocus()
                return@setOnClickListener
            }

            val postData = AddFeature(name)

            api.addFeature(postData)
                .enqueue(object : Callback<AddFeatureResponse>{
                    override fun onResponse(
                        call: Call<AddFeatureResponse>,
                        response: Response<AddFeatureResponse>
                    ) {
                        Log.e("didnot work", "onResponse: " + response.body()!!.errorMessage)
                        if (response.body()?.errorMessage!=null){
                            Toast.makeText(applicationContext, response.body()!!.errorMessage, Toast.LENGTH_LONG).show()
                        }
                        else{
                            Toast.makeText(applicationContext, "Feature "+response.body()!!.featureInfo.name+" has been created", Toast.LENGTH_LONG).show()
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<AddFeatureResponse>, t: Throwable) {
                        Log.e("didnot work", "onResponse: " + t.message)
                    }

                })

        }

        buttonCancelEditFeature.setOnClickListener {
            finish()
        }

    }

}
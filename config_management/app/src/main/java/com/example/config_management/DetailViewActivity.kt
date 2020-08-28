package com.example.config_management

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_detail.toolbartext
import kotlinx.android.synthetic.main.fragment_edit_feature.*
import kotlinx.android.synthetic.main.fragment_entity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewActivity : AppCompatActivity(){

    private val detailAdapter by lazy {DetailAdapter()}
    var oldparamextra:String? = null
    var idextras:Int = -1
    val api = ApiInterface.getClient().create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_detail)

        setSupportActionBar(toolbar2)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        idextras = intent.getIntExtra("id", -1)
        oldparamextra = intent.getStringExtra("oldparam")
        var name = intent.getStringExtra("name")

        tvDetailName.text = name

        if (oldparamextra=="Domains"){
            textView6.text = "Associated Features -> "
        }
        else{
            textView6.text = "Domains Associated to -> "

        }

        if(oldparamextra=="Domains"){
            toolbartext.text = "Domain Detail"
        }
        else
        {
            toolbartext.text = "Feature Detail"
        }

        recyclerview2.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@DetailViewActivity)
            adapter = detailAdapter
        }

        buttonEdit.setOnClickListener {
            if (oldparamextra=="Domains"){
                val intent = Intent(this, EditDomainActivity::class.java)
                intent.putExtra("id", idextras)
                this.startActivity(intent)
            }
            else{
                val intent = Intent(this, EditFeatureActivity::class.java)
                intent.putExtra("id", idextras)
                this.startActivity(intent)
            }
        }

        buttonDelete.setOnClickListener {
            val delete_id = idextras
            val api = ApiInterface.getClient().create(ApiService::class.java)

            fun onDeleteDomain(call: Call<DeleteDomain>) {
                call.enqueue(object : Callback<DeleteDomain> {
                    override fun onFailure(call: Call<DeleteDomain>, t: Throwable) {
                        Log.e("fail", "no_resp" + t.message)
                    }

                    override fun onResponse(call: Call<DeleteDomain>, response: Response<DeleteDomain>) {
                        println(response.body())
                        }
                    })
                }

            fun onDeleteFeature(call: Call<DeleteFeature>) {
                call.enqueue(object : Callback<DeleteFeature> {
                    override fun onFailure(call: Call<DeleteFeature>, t: Throwable) {
                        Log.e("fail", "no_resp" + t.message)
                    }

                    override fun onResponse(call: Call<DeleteFeature>, response: Response<DeleteFeature>) {
                        println(response.body())
                    }

                })
            }

            val myQuittingDialogBox: AlertDialog =
                AlertDialog.Builder(this) // set message, title, and icon
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to delete?")
                    .setPositiveButton("Delete",
                        DialogInterface.OnClickListener { dialog, _ -> //your deleting code

                            if (oldparamextra == "Domains") {
                                val call = api.deleteDomain(delete_id)
                                onDeleteDomain(call)
                            } else if (oldparamextra == "Features") {
                                val call = api.deleteFeature(delete_id)
                                onDeleteFeature(call)
                            }
                            finish()
                        })
                    .setNegativeButton("Cancel",
                        DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() })
                    .create()
            myQuittingDialogBox.show()

        }

    }

    override fun onResume() {
        super.onResume()
        hitApi()
    }

    private fun hitApi() {
        if (oldparamextra == "Domains") {
            val call = api.detailDomain(idextras)
            onDetailDomain(call)
        } else if (oldparamextra == "Features") {
            val call = api.detailFeature(idextras)
            onDetailFeature(call)
        }
    }


    private fun onDetailDomain(call: Call<DomainDetail>) {
        call.enqueue(object : Callback<DomainDetail> {
            override fun onResponse(call: Call<DomainDetail>, response: Response<DomainDetail>) {
                if (response.isSuccessful) {
//                    Log.e("worked", "onResponse: " + response.body())
                    response.body()?.domainInfo?.featureList?.let {
                        detailAdapter.setDetailData(it)
                        if (it.size==0) {
                            textView6.text = "No Features associated to this domain"
                        }
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
    private fun onDetailFeature(call: Call<FeatureDetail>) {
        call.enqueue(object : Callback<FeatureDetail> {
            override fun onResponse(call: Call<FeatureDetail>, response: Response<FeatureDetail>) {
                if (response.isSuccessful) {
//                    Log.e("worked", "onResponse: " + response.body())
                    response.body()?.featureInfo?.domainList?.let {
                        detailAdapter.setDetailData(it)
                        if (it.size==0) {
                            textView6.text = "Feature is not associated to any domain"
                        }
                    }


                } else {
                    Log.e("not working", "onResponse: ")
                }
            }

            override fun onFailure(call: Call<FeatureDetail>, t: Throwable) {
                Log.e("fail", "no_resp" + t.message)
            }

        })
    }

}
package com.example.config_management

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_entity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class EntityFragment : Fragment() {
    private var param1: String? = null
    private val myAdapter by lazy {
        MyAdapter({ id: Int, name: String ->
            val intent = Intent(view?.context, DetailViewActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("name", name)
            intent.putExtra("oldparam", param1)
            view?.context?.startActivity(intent)
        },


            {
                val delete_id = it
                val myQuittingDialogBox: AlertDialog =
                    AlertDialog.Builder(requireContext()) // set message, title, and icon
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete?")
                        .setPositiveButton("Delete",
                            DialogInterface.OnClickListener { dialog, _ -> //your deleting code

                                if (param1 == "Domains") {
                                    val call = api.deleteDomain(delete_id)
                                    onDeleteDomain(call)
                                } else if (param1 == "Features") {
                                    val call = api.deleteFeature(delete_id)
                                    onDeleteFeature(call)
                                }
                                dialog.dismiss()
                            })
                        .setNegativeButton("Cancel",
                            DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() })
                        .create()
                myQuittingDialogBox.show()
            },

            {
                if(param1 == "Domains"){
                    val intent = Intent(view?.context, EditDomainActivity::class.java)
                    intent.putExtra("id", it)
                    view?.context?.startActivity(intent)
                }
                else{
                    val intent = Intent(view?.context, EditFeatureActivity::class.java)
                    intent.putExtra("id", it)
                    view?.context?.startActivity(intent)
                }
            },

            {
                hitApi()
            })

    }

    val api = ApiInterface.getClient().create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_entity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = myAdapter
        }

        if (param1=="Features") {
            buttonAdd.setOnClickListener {
                val act = Intent(view?.context, AddFeatureActivity::class.java)
                view?.context?.startActivity(act)
            }
        }
        else{
            buttonAdd.setOnClickListener {
                val act = Intent(view?.context, AddDomainActivity::class.java)
                view?.context?.startActivity(act)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        hitApi()
    }

    private fun hitApi() {
        if (param1 == "Domains") {
            val call = api.fetchAllDomains()
            onDataReceivedDomain(call)
        } else if (param1 == "Features") {
            val call = api.fetchAllFeatures()
            onDataReceivedFeature(call)
        }
    }

    private fun onDataReceivedDomain(call: Call<DomainsInfo>) {
        call.enqueue(object : Callback<DomainsInfo> {
            override fun onResponse(call: Call<DomainsInfo>, response: Response<DomainsInfo>) {
                if (response.isSuccessful) {
//                    Log.e("worked", "onResponse: " + response.body())
                    response.body()?.domainsInfo?.let {
                        myAdapter.setData(it)
                    }
                } else {
                    Log.e("not working", "onResponse: ")
                }
            }

            override fun onFailure(call: Call<DomainsInfo>, t: Throwable) {
                Log.e("fail", "no_resp" + t.message)
            }

        })
    }

    private fun onDataReceivedFeature(call: Call<FeaturesInfo>) {
        call.enqueue(object : Callback<FeaturesInfo> {
            override fun onResponse(call: Call<FeaturesInfo>, response: Response<FeaturesInfo>) {
                if (response.isSuccessful) {
//                    Log.e("worked", "onResponse: " + response.body())
                    response.body()?.featuresInfo?.let {
                        myAdapter.setData(it)
                    }
                } else {
                    Log.e("not working", "onResponse: ")
                }
            }

            override fun onFailure(call: Call<FeaturesInfo>, t: Throwable) {
                Log.e("fail", "no_resp" + t.message)
            }

        })
    }

    private fun onDeleteDomain(call: Call<DeleteDomain>) {
        call.enqueue(object : Callback<DeleteDomain> {
            override fun onFailure(call: Call<DeleteDomain>, t: Throwable) {
                Log.e("fail", "no_resp" + t.message)
            }

            override fun onResponse(call: Call<DeleteDomain>, response: Response<DeleteDomain>) {
                println(response.body())
            }

        })
    }

    private fun onDeleteFeature(call: Call<DeleteFeature>) {
        call.enqueue(object : Callback<DeleteFeature> {
            override fun onFailure(call: Call<DeleteFeature>, t: Throwable) {
                Log.e("fail", "no_resp" + t.message)
            }

            override fun onResponse(call: Call<DeleteFeature>, response: Response<DeleteFeature>) {
                println(response.body())
            }

        })
    }

    companion object {

        fun newInstance(param1: String) =
            EntityFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)

                }
            }
    }
}


//intent class to switch to new page
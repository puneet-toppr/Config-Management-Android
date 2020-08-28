package com.example.config_management

import com.google.gson.annotations.SerializedName

data class DomainFeatureModel(
    @SerializedName("name")
    val name: String,

    @SerializedName("id")
    val id: Int
){
    var isSelected = false
}


data class DomainsInfo(

    @SerializedName("domains_info")
    val domainsInfo: List<DomainFeatureModel>
)

data class FeaturesInfo(

    @SerializedName("features_info")
    val featuresInfo: List<DomainFeatureModel>
)

data class DeleteDomain(

    @SerializedName("domain_info")
    val domainInfo: DomainFeatureModel
)

data class DeleteFeature(

    @SerializedName("feature_info")
    val featureInfo: DomainFeatureModel
)

//**************************************************************
//get one domain/feature details model
//**************************************************************
data class DomainDetail(

    @SerializedName("domain_info")
    val domainInfo: DomainInfo
)

data class DomainInfo(

    @SerializedName("name")
    val name: String,

    @SerializedName("id")
    val id : Int,

    @SerializedName("feature_id_list")
    val featureList : List<DomainFeatureModel>
)
//***************************************************************
data class FeatureDetail(

    @SerializedName("feature_info")
    val featureInfo: FeatureInfo
)

data class FeatureInfo(

    @SerializedName("name")
    val name: String,

    @SerializedName("id")
    val id : Int,

    @SerializedName("domain_id_list")
    val domainList : List<DomainFeatureModel>
)
//***************************************************************
data class AddFeatureResponse(

    @SerializedName("feature_info")
    val featureInfo: DomainFeatureModel,

    @SerializedName("error_message")
    val errorMessage: String
)

data class AddFeature(
    @SerializedName("feature_name")
    val name: String
)
//***************************************************************
data class AddDomainResponse(

    @SerializedName("domain_info")
    val domainInfo: DomainFeatureModel,

    @SerializedName("error_message")
    val errorMessage: String
)

data class AddDomain(

    @SerializedName("domain_name")
    val name: String,

    @SerializedName("feature_id_list")
    val featureIdList: List<Int>
)
//***************************************************************
data class EditFeatureResponse(

    @SerializedName("feature_info")
    val featureInfo: DomainFeatureModel,

    @SerializedName("error_message")
    val errorMessage: String
)

data class EditFeature(

    @SerializedName("feature_id")
    val id: Int,

    @SerializedName("feature_name")
    val name: String
)
//***************************************************************

data class EditDomainResponse(

    @SerializedName("feature_info")
    val domainInfo: DomainFeatureModel,

    @SerializedName("error_message")
    val errorMessage: String
)

data class EditDomain(

    @SerializedName("domain_id")
    val id: Int,

    @SerializedName("domain_name")
    val name: String,

    @SerializedName("feature_id_list")
    val featureIdList: List<Int>
)
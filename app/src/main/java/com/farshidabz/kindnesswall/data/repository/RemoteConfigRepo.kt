package com.farshidabz.kindnesswall.data.repository

//class RemoteConfigRepo(val context: Context, private var remoteConfig: FirebaseRemoteConfig) {
//    fun fetchFilterData(data: MutableLiveData<FilterRemoteModel>) {
//        remoteConfig.fetchAndActivate()
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    data.value = Gson().fromJson(
//                        remoteConfig.getString("filterConfig"),
//                        FilterRemoteModel::class.java
//                    )
//                } else {
//                    data.value = null
//                }
//            }
//            .addOnFailureListener {
//                data.value = null
//            }
//    }
//
//    fun getLanguagesFromRemoteConfig(data: MutableLiveData<BaseModel>) {
//        remoteConfig.fetchAndActivate()
//            .addOnCompleteListener {
//                AppPref.multiLanguageVariables = remoteConfig.getString("Languages")
//                data.value = BaseModel().apply { isSuccessful = true }
//            }.addOnCanceledListener {
//                data.value = BaseModel().apply { isSuccessful = false }
//            }
//    }
//
//    fun getMasseurPriceList(data: MutableLiveData<MasseurPriceListRemoteConfig>) {
//        remoteConfig.fetchAndActivate().addOnCompleteListener {
//            data.value = Gson().fromJson(
//                remoteConfig.getString("masseuse_price_list"),
//                MasseurPriceListRemoteConfig::class.java
//            )
//        }.addOnCanceledListener {
//            data.value = null
//        }
//    }
//
//    fun getIBANCurrencies(data: MutableLiveData<List<Currency>>) {
//        remoteConfig.fetchAndActivate().addOnCompleteListener {
//            data.value = Gson().fromJson(
//                remoteConfig.getString("IBAN_currencies"),
//                CurrencyRemoteConfigResponse::class.java
//            ).toNormalCurrency()
//        }.addOnCanceledListener {
//            data.value = null
//        }
//    }
//
//    fun getPenaltyRatio(data: MutableLiveData<Int>) {
//        remoteConfig.fetchAndActivate().addOnCompleteListener {
//            data.value = remoteConfig.getLong("penalty").toInt()
//        }.addOnCanceledListener {
//        }
//    }
//
//    fun getCommissionRatio(data: MutableLiveData<Double>) {
//        remoteConfig.fetchAndActivate().addOnCompleteListener {
//            data.value = remoteConfig.getDouble("commission")
//        }.addOnCanceledListener {
//        }
//    }
//
//    fun getWithdrawMinAmount(data: MutableLiveData<Int>) {
//        remoteConfig.fetchAndActivate().addOnCompleteListener {
//            data.value = remoteConfig.getLong("withdrawMinAmount").toInt()
//        }.addOnCanceledListener {
//        }
//    }
//
//    fun getPriceScheduleConfig(data: MutableLiveData<ReschedulePriceConfigModel>) {
//        remoteConfig.fetchAndActivate().addOnCompleteListener {
//            data.value = Gson().fromJson(
//                remoteConfig.getString("reschedulePrice"),
//                ReschedulePriceConfigModel::class.java
//            )
//        }.addOnCanceledListener {
//            Log.e(">>>>", "")
//        }.addOnFailureListener {
//            Log.e(">>>>", "")
//        }
//    }
//
//    fun getUpdates(data: @UpdateType MutableLiveData<Int>) {
//        remoteConfig.fetchAndActivate().addOnCompleteListener {
//            val minVersion = remoteConfig.getLong("min_version")
//            val currentVersion = remoteConfig.getLong("current_version")
//            val applicationVersion = BuildConfig.VERSION_CODE
//
//            if (applicationVersion < minVersion) {
//                data.value = UpdateType.FORCE_UPDATE
//                return@addOnCompleteListener
//            }
//
//            if (applicationVersion < currentVersion) {
//                data.value = UpdateType.OPTIONAL_UPDATE
//                return@addOnCompleteListener
//            }
//            data.value = UpdateType.UPDATE_IS_NOT_NECESSARY
//        }.addOnCanceledListener {
//            data.value = UpdateType.EEROR_IN_GETTING_DATA
//        }.addOnFailureListener {
//            data.value = UpdateType.EEROR_IN_GETTING_DATA
//        }
//    }
//
//    fun getAttributes(
//        attributeName: String,
//        data: MutableLiveData<RemoteConfigAttributesResponseModel>
//    ) {
//        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                if (attributeName == LANGUAGE) {
//                    data.value = Gson().fromJson(
//                        remoteConfig.getString("speak_language"),
//                        RemoteConfigAttributesResponseModel::class.java
//                    )
//                } else if (attributeName == SMOKING) {
//                    data.value = Gson().fromJson(
//                        remoteConfig.getString("smoking_habit"),
//                        RemoteConfigAttributesResponseModel::class.java
//                    )
//                }
//            } else {
//                data.value = null
//            }
//        }.addOnCanceledListener {
//            data.value = null
//        }.addOnFailureListener {
//            data.value = null
//        }
//    }
//
//    fun getMoreInfo(data: MutableLiveData<MoreInfoModel>) {
//        remoteConfig.fetchAndActivate()
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    data.value = Gson().fromJson(
//                        remoteConfig.getString("more_info"),
//                        MoreInfoModel::class.java
//                    )
//
//                } else {
//                    data.value = null
//                }
//            }
//            .addOnFailureListener {
//                data.value = null
//            }
//    }
//}
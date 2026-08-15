package com.example.kuitandroidapiexample.home.viewmodel

import com.example.kuitandroidapiexample.data.dto.response.ResponseAnimalDetailWrapper
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.kuitandroidapiexample.data.ServicePool
import com.example.kuitandroidapiexample.data.dto.ResponseAnimalListDto
import com.example.kuitandroidapiexample.data.dto.request.RequestAnimalDto
import com.example.kuitandroidapiexample.data.dto.response.ResponseAnimalDetailDto
import com.example.kuitandroidapiexample.data.service.HomeService
import com.example.kuitandroidapiexample.model.UiAnimal
import com.example.kuitandroidapiexample.model.toUi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val api: HomeService = ServicePool.homeService

    /* ───────── 목록 상태 ───────── */
    private val _list = mutableStateOf<List<UiAnimal>>(emptyList())
    val listState: State<List<UiAnimal>> = _list

    fun getTotalAnimalList() {
        api.getTotalAnimalList()
            .enqueue(object : Callback<ResponseAnimalListDto> {
                override fun onResponse(
                    call: Call<ResponseAnimalListDto>,
                    response: Response<ResponseAnimalListDto>
                ) {
                    if (response.isSuccessful) {
                        _list.value = response.body()!!.data.map { it.toUi() }
                    } else {
                        Log.e("LIST", "${response.code()} ${response.message()}")
                    }
                }
                override fun onFailure(call: Call<ResponseAnimalListDto>, t: Throwable) {
                    Log.e("LIST", "fail: ${t.message}")
                }
            })
    }

    /* ───────── 상세 상태 ───────── */
    private val _detail = mutableStateOf<UiAnimal?>(null)
    val detailState: State<UiAnimal?> = _detail

    fun getAnimalDetail(id: Int) {
        api.getAnimalDetail(id)
            .enqueue(object : Callback<ResponseAnimalDetailWrapper> {
                override fun onResponse(
                    c: Call<ResponseAnimalDetailWrapper>,
                    r: Response<ResponseAnimalDetailWrapper>
                ) {
                    if (r.isSuccessful) {
                        _detail.value = r.body()!!.data.toUi()   // ★ .data
                    } else {
                        Log.e("DETAIL", "${r.code()} ${r.message()}")
                    }
                }
                override fun onFailure(c: Call<ResponseAnimalDetailWrapper>, t: Throwable) {
                    Log.e("DETAIL", t.message ?: "")
                }
            })
    }

    /* ───────── 등록(POST) ───────── */
    fun registerAnimal(req: RequestAnimalDto, onResult: (Boolean) -> Unit) =
        api.registerAnimal(req).enqueue(boolCallback(onResult))

    /* ───────── 삭제(DELETE) ───────── */
    fun deleteAnimal(id: Int, onResult: (Boolean) -> Unit) =
        api.deleteAnimal(id).enqueue(boolCallback(onResult))

    /* ───────── 공용 콜백 ───────── */
    private fun boolCallback(cb: (Boolean) -> Unit) =
        object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) =
                cb(response.isSuccessful)

            override fun onFailure(call: Call<Void>, t: Throwable) =
                cb(false)
        }
}

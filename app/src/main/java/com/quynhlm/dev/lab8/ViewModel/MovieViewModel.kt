import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quynhlm.dev.lab8.Model.Movie
import com.quynhlm.dev.lab8.Service.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {
    private val _productState = mutableStateOf<List<Movie>?>(null)
    val productState: State<List<Movie>?> = _productState
    private val _registerState = mutableStateOf<Boolean?>(null)
    val registerState: State<Boolean?> = _registerState

    fun getAllProduct() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getListMovies()
                if(response.isSuccessful){
                    _productState.value = response.body()
                }else{
                    _productState.value = null
                }
            }catch (e : Exception){
                _productState.value = null
            }
        }
    }

    fun addMovie (movie: Movie){
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.addMovie(movie)
                if (response.isSuccessful) {
                    _registerState.value = true
                    Log.e("TAG", "registerUser Body: "+ response.body())
                    getAllProduct()
                } else {
                    _registerState.value = false
                    Log.e("Tag","registerUser : " + response.code())
                }
            } catch (e: Exception) {
                _registerState.value = false
                Log.e("TAG", "registerUser: "+ e.message)
            }
        }
    }

    fun updateMovie (id : String , movie: Movie) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.updateMovie(id, movie)
                if (response.isSuccessful) {
                    _registerState.value = true
                    Log.e("TAG", "registerUser Body: "+ response.body())
                    getAllProduct()
                } else {
                    _registerState.value = false
                    Log.e("Tag","registerUser : " + response.code())
                }
            }catch (e :Exception) {
                _registerState.value = false
                Log.e("TAG", "registerUser: "+ e.message)
            }
        }
    }
}

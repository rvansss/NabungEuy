import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.evan0107.nabungeuy.database.CitaCitaDao
import com.evan0107.nabungeuy.saving.SavingViewModel

class SavingViewModelFactory(private val dao: CitaCitaDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SavingViewModel::class.java)) {
            return SavingViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

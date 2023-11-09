package g58089.mobg5.remise1.ui

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class Remise1ViewModelTest {
    private val viewModel = Remise1ViewModel()

    @Test
    fun remise1ViewModel_CheckUserEmail_Correct() {
        viewModel.updateUserEmail("58089@etu.he2b.be")
        viewModel.checkUserEmail()
        assertFalse(viewModel.uiState.isEmailWrong)
        assertTrue(viewModel.uiState.isLoginSuccessful)
    }

    @Test
    fun remise1ViewModel_CheckUserEmail_Incorrect() {
        viewModel.updateUserEmail("sdfghjklm@dv")
        viewModel.checkUserEmail()
        assertTrue(viewModel.uiState.isEmailWrong)
        assertFalse(viewModel.uiState.isLoginSuccessful)
    }

    @Test
    fun remise1ViewModel_CheckUserEmail_Empty() {
        viewModel.updateUserEmail("")
        viewModel.checkUserEmail()
        assertTrue(viewModel.uiState.isEmailWrong)
        assertFalse(viewModel.uiState.isLoginSuccessful)
    }
}
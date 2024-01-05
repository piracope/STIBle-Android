package g58089.mobg5.stible.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import g58089.mobg5.stible.data.GameHistoryRepository
import g58089.mobg5.stible.data.dto.GameRecap
import kotlinx.coroutines.launch

class StatsScreenViewModel(private val gameHistoryRepository: GameHistoryRepository) : ViewModel() {
    private val historyData = mutableStateListOf<GameRecap>()

    private val _gameRecapGuessCount = mutableStateMapOf<String, Int>()
    val gameRecapGuessCount: Map<String, Int>
        get() = _gameRecapGuessCount

    val numberOfGames: Int
        get() = historyData.size

    var bestStreak by mutableIntStateOf(0)
    var currentStreak by mutableIntStateOf(0)

    private var numberOfLosses: Int by mutableIntStateOf(0)
    private val numberOfWins: Int
        get() = numberOfGames - numberOfLosses

    val winRate: Double
        get() = numberOfWins / (numberOfGames * 1.0)

    init {
        viewModelScope.launch {
            gameHistoryRepository.getAllRecapsStream().collect {
                historyData.clear()
                historyData.addAll(it)
                update()
            }
        }
    }

    private fun Int.isNext(previous: Int) = this == previous + 1
    private fun hasLost(recap: GameRecap) = recap.bestPercentage != 1.0

    private fun update() {
        _gameRecapGuessCount.clear()

        var lastSeenPercentage = 0.0
        var lastSeenPuzzle = 0
        var best = 0
        var current = 0

        for (recap in historyData) {
            // Checking if we skipped a day or lost today
            if (!recap.puzzleNumber.isNext(lastSeenPuzzle) || hasLost(recap)) {
                best = maxOf(best, current)
                current = 0
            } else {
                current++
            }

            // Updating the last seen puzzle number
            lastSeenPuzzle = recap.puzzleNumber

            // Updating loss count
            if (hasLost(recap)) {
                numberOfLosses++
            }

            // Incrementing the guess count thingie for the bar chart
            val guessCount = if (hasLost(recap)) "X" else recap.guessCount.toString()

            _gameRecapGuessCount[guessCount] =
                _gameRecapGuessCount.getOrDefault(guessCount, 0) + 1
        }

        // Edge case : he won last time (current = x) but last time was BEFORE yesterday
        // -> should be 0

        // FIXME: NO WAY OF CHECKING THAT WITHOUT HAVING ACCESS TO GAME RULES !!!!!!

        currentStreak = current
        bestStreak = best
    }
}
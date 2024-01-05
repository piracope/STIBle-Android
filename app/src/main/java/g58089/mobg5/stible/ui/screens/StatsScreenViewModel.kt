package g58089.mobg5.stible.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import g58089.mobg5.stible.data.GameHistoryRepository
import g58089.mobg5.stible.data.UserPreferencesRepository
import g58089.mobg5.stible.data.dto.GameRecap
import g58089.mobg5.stible.data.dto.Stop
import g58089.mobg5.stible.data.dto.UserPreferences
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * [ViewModel] handling presentation logic for the statistics screen.
 */
class StatsScreenViewModel(
    private val gameHistoryRepository: GameHistoryRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    companion object {
        const val FLOAT_LOSS_MARKER = 0.0f
    }

    /**
     * All previously recorded [GameRecap]s.
     */
    private val historyData = mutableStateListOf<GameRecap>()

    /**
     * All key-value pairs stored in the user's device.
     */
    private var userPreferences by mutableStateOf(UserPreferences())

    /**
     * A [Map] of the repartition of number of guesses it took to finish each game.
     *
     * Looks like this :
     * {"X": 2, "1", 5, ... "6": 8}
     */
    val guessCountRepartition: Map<Int, Int>
        get() = _gameRecapGuessCount
    private val _gameRecapGuessCount = mutableStateMapOf<Int, Int>()

    /**
     * Total number of [GameRecap]s stored in the device.
     */
    val numberOfGames: Int
        get() = historyData.size

    /**
     * The longest streak of winning games the player has made.
     *
     * A streak is broken when a. the player loses and b. the player misses a day.
     */
    var bestStreak by mutableIntStateOf(0)
        private set

    /**
     * The current streak of winning games the player has made.
     */
    var currentStreak by mutableIntStateOf(0)
        private set

    /**
     * The number of games the player has lost, used to compute [numberOfWins].
     */
    private var numberOfLosses: Int by mutableIntStateOf(0)

    /**
     * The number of games the player has won
     */
    private val numberOfWins: Int
        get() = numberOfGames - numberOfLosses

    /**
     * The win rate, the percentage of times the player won
     */
    val winRate: Double
        get() = numberOfWins / (numberOfGames * 1.0)

    val stopsInHistory: List<Stop>
        get() = historyData.map { it.mysteryStop }

    init {
        viewModelScope.launch {
            combine(
                gameHistoryRepository.getAllRecapsStream(),
                userPreferencesRepository.userData
            ) { session, pref ->
                userPreferences = pref

                historyData.clear()
                historyData.addAll(session)
                update()
            }.collect()
        }
    }

    /**
     * Checks if this [Int] directly follows a given [Int]
     */
    private fun Int.isNext(previous: Int) = this == previous + 1

    /**
     * Checks if a [GameRecap] is of a lost game.
     *
     * A game is lost if the best proximity percentage hasn't achieved 100%.
     */
    private fun hasLost(recap: GameRecap) = recap.bestPercentage != 1.0

    /**
     * Restores the [guessCountRepartition] back to its default state of 1 -> 0, 2 -> 0, ...
     */
    private fun MutableMap<Int, Int>.backToDefault() {
        this.clear()
        for (i in 0..userPreferences.maxGuessCount) {
            this[i] = 0
        }
    }

    /**
     * Updates all values when a change occurs in the game history.
     */
    private fun update() {
        _gameRecapGuessCount.backToDefault()

        var lastSeenPuzzle =
            if (historyData.isNotEmpty()) historyData.first().puzzleNumber - 1 else 0
        var best = 0
        var current = 0

        for (recap in historyData) {
            // Checking if we skipped a day or lost today
            if (!recap.puzzleNumber.isNext(lastSeenPuzzle) || hasLost(recap)) {
                best = maxOf(best, current)
                current = 0
            }

            if (!hasLost(recap)) {
                current++
            }

            // Updating the last seen puzzle number
            lastSeenPuzzle = recap.puzzleNumber

            // Updating loss count
            if (hasLost(recap)) {
                numberOfLosses++
            }

            // Incrementing the guess count thingie for the bar chart
            val guessCount = if (hasLost(recap)) 0 else recap.guessCount

            _gameRecapGuessCount[guessCount] =
                _gameRecapGuessCount.getOrDefault(guessCount, 0) + 1
        }

        best = maxOf(best, current)

        // Edge case : he won last time (current = x) but last time was BEFORE yesterday
        // -> should be 0
        if (lastSeenPuzzle < userPreferences.lastSeenPuzzleNumber - 1) {
            current = 0
        }

        currentStreak = current
        bestStreak = best
    }
}
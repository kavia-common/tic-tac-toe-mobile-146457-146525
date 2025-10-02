package org.example.app

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.button.MaterialButton

/**
 * PUBLIC_INTERFACE
 * MainActivity hosts the Tic Tac Toe game UI and orchestrates user interactions.
 *
 * Purpose:
 * - Display a centered 3x3 board.
 * - Handle user taps to place X or O.
 * - Show game status (turn, winner, draw).
 * - Provide a restart control and score display.
 *
 * Parameters:
 * - savedInstanceState: Bundle? - standard Android state.
 *
 * Returns:
 * - None. Side-effects update the on-screen UI.
 */
class MainActivity : Activity() {

    private lateinit var statusView: TextView
    private lateinit var scoreXView: TextView
    private lateinit var scoreOView: TextView
    private lateinit var tiles: List<TextView>
    private lateinit var restartButton: MaterialButton

    // Game model
    private val board = Array(9) { ' ' }
    private var currentPlayer = 'X'
    private var isGameOver = false
    private var scoreX = 0
    private var scoreO = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()
        setupBoardListeners()
        updateStatus()
        updateScores()
    }

    private fun bindViews() {
        statusView = findViewById(R.id.tvStatus)
        scoreXView = findViewById(R.id.tvScoreX)
        scoreOView = findViewById(R.id.tvScoreO)

        tiles = listOf(
            findViewById(R.id.tile0),
            findViewById(R.id.tile1),
            findViewById(R.id.tile2),
            findViewById(R.id.tile3),
            findViewById(R.id.tile4),
            findViewById(R.id.tile5),
            findViewById(R.id.tile6),
            findViewById(R.id.tile7),
            findViewById(R.id.tile8),
        )

        restartButton = findViewById(R.id.btnRestart)
        restartButton.setOnClickListener {
            resetGame()
        }

        // Accessibility content descriptions
        tiles.forEachIndexed { index, tv ->
            tv.contentDescription = getString(R.string.content_tile, index)
        }
    }

    private fun setupBoardListeners() {
        tiles.forEachIndexed { index, textView ->
            textView.setOnClickListener {
                onTileTapped(index)
            }
        }
    }

    // PUBLIC_INTERFACE
    /**
     * Makes a move on the specified index if allowed.
     * @param index Board index [0..8]
     */
    fun onTileTapped(index: Int) {
        if (isGameOver) return
        if (board[index] != ' ') return

        board[index] = currentPlayer
        tiles[index].text = currentPlayer.toString()

        if (checkWin(currentPlayer)) {
            isGameOver = true
            onWin(currentPlayer)
        } else if (board.all { it != ' ' }) {
            isGameOver = true
            onDraw()
        } else {
            togglePlayer()
            updateStatus()
        }
    }

    private fun togglePlayer() {
        currentPlayer = if (currentPlayer == 'X') 'O' else 'X'
    }

    private fun onWin(player: Char) {
        if (player == 'X') scoreX++ else scoreO++
        updateScores()
        statusView.text = if (player == 'X') getString(R.string.status_x_wins) else getString(R.string.status_o_wins)
        highlightWinningLine(player)
        // Disable further interaction after win
    }

    private fun onDraw() {
        statusView.text = getString(R.string.status_draw)
    }

    private fun updateStatus() {
        statusView.text = if (currentPlayer == 'X') getString(R.string.status_turn_x) else getString(R.string.status_turn_o)
    }

    private fun updateScores() {
        scoreXView.text = scoreX.toString()
        scoreOView.text = scoreO.toString()
    }

    // PUBLIC_INTERFACE
    /**
     * Resets the current game state while keeping the score.
     */
    fun resetGame() {
        for (i in board.indices) {
            board[i] = ' '
            tiles[i].text = ""
            tiles[i].isEnabled = true
            // Remove highlight
            tiles[i].setTextColor(getColorCompat(android.R.color.black))
        }
        isGameOver = false
        currentPlayer = 'X'
        updateStatus()
    }

    private fun getColorCompat(resId: Int): Int {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            resources.getColor(resId, theme)
        } else {
            @Suppress("DEPRECATION")
            resources.getColor(resId)
        }
    }

    private fun checkWin(player: Char): Boolean {
        val b = board
        val lines = arrayOf(
            intArrayOf(0, 1, 2),
            intArrayOf(3, 4, 5),
            intArrayOf(6, 7, 8),
            intArrayOf(0, 3, 6),
            intArrayOf(1, 4, 7),
            intArrayOf(2, 5, 8),
            intArrayOf(0, 4, 8),
            intArrayOf(2, 4, 6)
        )
        return lines.any { (a, c, d) ->
            b[a] == player && b[c] == player && b[d] == player
        }
    }

    private fun findWinningLine(player: Char): IntArray? {
        val b = board
        val lines = arrayOf(
            intArrayOf(0, 1, 2),
            intArrayOf(3, 4, 5),
            intArrayOf(6, 7, 8),
            intArrayOf(0, 3, 6),
            intArrayOf(1, 4, 7),
            intArrayOf(2, 5, 8),
            intArrayOf(0, 4, 8),
            intArrayOf(2, 4, 6)
        )
        for (line in lines) {
            val (a, c, d) = line
            if (b[a] == player && b[c] == player && b[d] == player) {
                return line
            }
        }
        return null
    }

    private fun highlightWinningLine(player: Char) {
        val line = findWinningLine(player) ?: return
        val color = if (player == 'X') getColorCompat(R.color.ocean_primary) else getColorCompat(R.color.ocean_secondary)
        line.forEach { index ->
            tiles[index].setTextColor(color)
        }
        // Optionally disable tiles after win
        tiles.forEach { it.isEnabled = false }
    }
}

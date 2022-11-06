package com.example.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.game.data.Game
import com.example.game.data.getFormattedPrice
import com.example.game.databinding.FragmentGameDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class GameDetailFragment : Fragment() {
    private val navigationArgs: GameDetailFragmentArgs by navArgs()
    lateinit var game: Game

    private val viewModel: GameViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as GameApplication).database.gameDao()
        )
    }

    private var _binding: FragmentGameDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun bind(game: Game) {
        binding.apply {
            gameName.text = game.gameName
            gamePrice.text = game.getFormattedPrice()
            gameCount.text = game.quantityInStock.toString()
            sellGame.isEnabled = viewModel.isStockAvailable(game)
            sellGame.setOnClickListener { viewModel.sellGame(game) }
            deleteGame.setOnClickListener { showConfirmationDialog() }
            editGame.setOnClickListener { editGame() }
        }
    }

    private fun editGame() {
        val action = GameDetailFragmentDirections.actionGameDetailFragmentToAddGameFragment(
            getString(R.string.edit_fragment_title),
            game.id
        )
        this.findNavController().navigate(action)
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteGame()
            }
            .show()
    }

    private fun deleteGame() {
        viewModel.deleteGame(game)
        findNavController().navigateUp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.gameId
        viewModel.retrieveGame(id).observe(this.viewLifecycleOwner) { selectedGame ->
            game = selectedGame
            bind(game)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.example.game

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.game.data.Game
import com.example.game.databinding.FragmentAddGameBinding

class AddGameFragment : Fragment() {

    private val viewModel: GameViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as GameApplication).database
                .gameDao()
        )
    }
    private val navigationArgs: GameDetailFragmentArgs by navArgs()

    lateinit var game: Game

    private var _binding: FragmentAddGameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.gameName.text.toString(),
            binding.gamePrice.text.toString(),
            binding.gameCount.text.toString(),
        )
    }

    private fun bind(game: Game) {
        val price = "%.2f".format(game.gamePrice)
        binding.apply {
            gameName.setText(game.gameName, TextView.BufferType.SPANNABLE)
            gamePrice.setText(price, TextView.BufferType.SPANNABLE)
            gameCount.setText(game.quantityInStock.toString(), TextView.BufferType.SPANNABLE)
            saveAction.setOnClickListener { updateGame() }
        }
    }

    private fun addNewGame() {
        if (isEntryValid()) {
            viewModel.addNewGame(
                binding.gameName.text.toString(),
                binding.gamePrice.text.toString(),
                binding.gameCount.text.toString(),
            )
            val action = AddGameFragmentDirections.actionAddGameFragmentToGameListFragment()
            findNavController().navigate(action)
        }
    }
    
    private fun updateGame() {
        if (isEntryValid()) {
            viewModel.updateGame(
                this.navigationArgs.gameId,
                this.binding.gameName.text.toString(),
                this.binding.gamePrice.text.toString(),
                this.binding.gameCount.text.toString()
            )
            val action = AddGameFragmentDirections.actionAddGameFragmentToGameListFragment()
            findNavController().navigate(action)
        }
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.gameId
        if (id > 0) {
            viewModel.retrieveGame(id).observe(this.viewLifecycleOwner) { selectedGame ->
                game = selectedGame
                bind(game)
            }
        } else {
            binding.saveAction.setOnClickListener {
                addNewGame()
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
}

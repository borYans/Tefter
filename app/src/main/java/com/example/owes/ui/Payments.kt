package com.example.owes.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.owes.R
import com.example.owes.data.adapters.PaymentsRecyclerAdapter
import com.example.owes.data.model.entities.Debtor
import com.example.owes.utils.Constants.NEGATIVE_NUMBER
import com.example.owes.utils.Constants.POSITIVE_NUMBER
import com.example.owes.utils.DebtorOnClickListener
import com.example.owes.utils.OwesSharedPrefs.initSharedPrefs
import com.example.owes.utils.OwesSharedPrefs.readFromPrefs
import com.example.owes.viewmodels.DebtorViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_paid_debts.*
import kotlinx.android.synthetic.main.fragment_payments.*

@AndroidEntryPoint
class Payments : Fragment(R.layout.fragment_payments), DebtorOnClickListener {

    private val paymentsRecyclerAdapter = PaymentsRecyclerAdapter(this)
    private val debtorViewModel: DebtorViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSharedPrefs(requireContext())
        initializeAdapter()
        initializeAdMobAds()

        listenToAddNewPaymentClick()
        showAllPayments()


        val itemTouchHelpeCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position= viewHolder.adapterPosition
                val debtor = paymentsRecyclerAdapter.differ.currentList[position]
                debtorViewModel.deletePayment(debtor)
                askDeleteConfirmation(debtor)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelpeCallback)
        itemTouchHelper.attachToRecyclerView(paymentsRecyclerView)

    }

    private fun askDeleteConfirmation(debtor: Debtor) {
        Snackbar.make(requireView(), getString(R.string.payment_deleted), Snackbar.LENGTH_LONG).apply {
            setBackgroundTint(resources.getColor(android.R.color.holo_red_light))
            setTextColor(resources.getColor(R.color.white))
            setActionTextColor(resources.getColor(R.color.white))
            setAction(getString(R.string.undo)) {
                debtorViewModel.addDebtor(debtor)
            }
        }
            .show()
    }


    private fun showTotalMoney() {
        debtorViewModel.calculateTotal().observe(viewLifecycleOwner, { money ->
            money?.let {
                if (money.containsKey(POSITIVE_NUMBER)) {
                    sumOfMoneyAmount.setTextColor(context?.resources?.getColor(android.R.color.holo_green_dark)!!)
                    sumOfMoneyAmount.text = "+${readFromPrefs(getString(R.string.CURRENCY), getString(R.string.DOLLAR))}${money.getValue(POSITIVE_NUMBER)}"
                }

                if (money.containsKey(NEGATIVE_NUMBER)) {
                    sumOfMoneyAmount.setTextColor(context?.resources?.getColor(android.R.color.holo_red_dark)!!)
                    sumOfMoneyAmount.text = "-${readFromPrefs(getString(R.string.CURRENCY), getString(R.string.DOLLAR))}${money.getValue(NEGATIVE_NUMBER)}"
                }
            }
        })
    }

    private fun showAllPayments() {
        debtorViewModel.getAllPayments().observe(viewLifecycleOwner, { debtors ->
           debtors?.let {
               validatePaymentsList(it)
               paymentsRecyclerAdapter.differ.submitList(it)
               showTotalMoney()
           }
       })
    }

    private fun validatePaymentsList(it: List<Debtor>) {
        if (it.isEmpty()) {
            noPaymentsInfoTxt.visibility = View.VISIBLE
        } else {
            noPaymentsInfoTxt.visibility = View.GONE
        }
    }

    private fun listenToAddNewPaymentClick() {
        addPaymentButton.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(PaymentsDirections.actionPaymentsToAddPayment())
        }
    }

    private fun initializeAdMobAds() {
        MobileAds.initialize(requireContext()) {}
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    private fun initializeAdapter() {
        val dividerItemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        paymentsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = paymentsRecyclerAdapter
            addItemDecoration(dividerItemDecoration)
        }
    }


    override fun onDebtorClick(debtor_name: String) {
        Navigation.findNavController(requireView()).navigate(PaymentsDirections.actionPaymentsToDebtorDetail(debtor_name))
    }
}
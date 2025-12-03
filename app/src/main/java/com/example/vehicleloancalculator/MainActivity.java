package com.example.vehicleloancalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private EditText etVehiclePrice, etDownPayment, etLoanPeriod, etInterestRate;
    private TextView tvLoanAmount, tvTotalInterest, tvTotalPayment, tvMonthlyPayment;
    private LinearLayout resultsLayout;
    private Button btnCalculate;

    private DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize all views
        initializeViews();

        // Setup calculate button
        setupCalculateButton();
    }

    private void initializeViews() {
        etVehiclePrice = findViewById(R.id.etVehiclePrice);
        etDownPayment = findViewById(R.id.etDownPayment);
        etLoanPeriod = findViewById(R.id.etLoanPeriod);
        etInterestRate = findViewById(R.id.etInterestRate);

        tvLoanAmount = findViewById(R.id.tvLoanAmount);
        tvTotalInterest = findViewById(R.id.tvTotalInterest);
        tvTotalPayment = findViewById(R.id.tvTotalPayment);
        tvMonthlyPayment = findViewById(R.id.tvMonthlyPayment);

        resultsLayout = findViewById(R.id.resultsLayout);
        btnCalculate = findViewById(R.id.btnCalculate);
    }

    private void setupCalculateButton() {
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateLoan();
            }
        });
    }

    private void calculateLoan() {
        try {
            // Get input values
            String vehiclePriceStr = etVehiclePrice.getText().toString();
            String downPaymentStr = etDownPayment.getText().toString();
            String loanPeriodStr = etLoanPeriod.getText().toString();
            String interestRateStr = etInterestRate.getText().toString();

            // Check if any field is empty
            if (vehiclePriceStr.isEmpty() || downPaymentStr.isEmpty() ||
                    loanPeriodStr.isEmpty() || interestRateStr.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Convert to numbers
            double vehiclePrice = Double.parseDouble(vehiclePriceStr);
            double downPayment = Double.parseDouble(downPaymentStr);
            int loanPeriod = Integer.parseInt(loanPeriodStr);
            double interestRate = Double.parseDouble(interestRateStr);

            // Validate inputs
            if (downPayment >= vehiclePrice) {
                Toast.makeText(this, "Down payment must be less than vehicle price", Toast.LENGTH_SHORT).show();
                return;
            }

            if (loanPeriod <= 0) {
                Toast.makeText(this, "Loan period must be greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }

            // Perform calculations (EXACTLY as per assignment)
            double loanAmount = vehiclePrice - downPayment;
            double totalInterest = loanAmount * (interestRate / 100) * loanPeriod;
            double totalPayment = loanAmount + totalInterest;
            double monthlyPayment = totalPayment / (loanPeriod * 12);

            // Display results
            displayResults(loanAmount, totalInterest, totalPayment, monthlyPayment);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "An error occurred during calculation", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayResults(double loanAmount, double totalInterest, double totalPayment, double monthlyPayment) {
        tvLoanAmount.setText("RM " + decimalFormat.format(loanAmount));
        tvTotalInterest.setText("RM " + decimalFormat.format(totalInterest));
        tvTotalPayment.setText("RM " + decimalFormat.format(totalPayment));
        tvMonthlyPayment.setText("RM " + decimalFormat.format(monthlyPayment));

        resultsLayout.setVisibility(View.VISIBLE);
    }

    // Create options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Handle menu item clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_about) {
            Intent intent = new Intent(this, com.example.vehicleloancalculator.AboutActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.menu_clear) {
            clearAllFields();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearAllFields() {
        etVehiclePrice.setText("");
        etDownPayment.setText("");
        etLoanPeriod.setText("");
        etInterestRate.setText("");
        resultsLayout.setVisibility(View.GONE);
        Toast.makeText(this, "All fields cleared", Toast.LENGTH_SHORT).show();
    }
}
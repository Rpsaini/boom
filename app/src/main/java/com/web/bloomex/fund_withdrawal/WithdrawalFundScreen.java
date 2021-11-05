package com.web.bloomex.fund_withdrawal;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.app.dialogsnpickers.DialogCallBacks;
import com.web.bloomex.BaseActivity;
import com.web.bloomex.DefaultConstants;
import com.web.bloomex.R;
import com.web.bloomex.ScanQrCode;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class WithdrawalFundScreen extends BaseActivity {
    private TextView proceedBT = null;
    private ImageView backIC = null;
    private ImageView scanImgIC;
    private EditText BTCAmountET, withdrawalFeesET, finalAmountET, remarksET, destinationAddressET;
    private TextView BTCAmountTV, amountTV, tvTitle;
    private String symbol;
    double fee;
    String comparisionSymbol = "XRP";
    String fee_type = "";
    public NumberFormat formatter = new DecimalFormat("#0.0000");

    private double availableBal = 0;
    private TextView desTagTV;
    private RelativeLayout desTagRL;
    private EditText desfinalAmountET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.withdraw_screen);
        initiateObj();
        init();
        setOnClickListener();
    }
//hghjgchhhh
    private void init() {
        try {
            proceedBT = findViewById(R.id.proceedBT);
            backIC = findViewById(R.id.backIC);
            scanImgIC = findViewById(R.id.scanImgIC);
            destinationAddressET = findViewById(R.id.destinationAddressET);
            BTCAmountET = findViewById(R.id.BTCAmountET);
            withdrawalFeesET = findViewById(R.id.withdrawalFeesET);
            finalAmountET = findViewById(R.id.finalAmountET);
            remarksET = findViewById(R.id.remarksET);
            BTCAmountTV = findViewById(R.id.BTCAmountTV);
            amountTV = findViewById(R.id.amountTV);
            tvTitle = findViewById(R.id.tvTitle);
            desTagTV = findViewById(R.id.desTagTV);
            desTagRL = findViewById(R.id.desTagRL);
            desfinalAmountET = findViewById(R.id.desfinalAmountET);

              //show destination tag
            JSONObject data = new JSONObject(getIntent().getStringExtra("data"));

            availableBal = Double.parseDouble(data.getString("available_balance"));
            symbol = data.getString("symbol");


            fee = Double.parseDouble(data.getString("fee"));
            fee_type = data.getString("fee_type");

            tvTitle.setText(getResources().getString(R.string.withdraw) + " " + symbol);
            BTCAmountTV.setText("Enter " + symbol);
            amountTV.setText(availableBal + " " + symbol);

            if (fee_type.equalsIgnoreCase("fixed")) {
                withdrawalFeesET.setText(data.getString("fee") + " " + fee_type);
            } else {
                withdrawalFeesET.setText(data.getString("fee") + "%");
            }
            if (symbol.equalsIgnoreCase(comparisionSymbol)) {
                desTagTV.setVisibility(View.VISIBLE);
                desTagRL.setVisibility(View.VISIBLE);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setOnClickListener() {
        backIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        proceedBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validationRule.checkEmptyString(destinationAddressET) == 0) {
                    alertDialogs.alertDialog(WithdrawalFundScreen.this, getResources().getString(R.string.Response), "Enter Destination address.", getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {
                        }
                    });
                    return;
                }

                if (validationRule.checkEmptyString(BTCAmountET) == 0) {
                    alertDialogs.alertDialog(WithdrawalFundScreen.this, getResources().getString(R.string.Response), "Enter " + symbol + " Withdraw amount.", getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {
                        }
                    });
                    return;
                }
                if (availableBal < Double.parseDouble(finalAmountET.getText().toString())) {
                    alertDialogs.alertDialog(WithdrawalFundScreen.this, getResources().getString(R.string.Response), "Withdraw amount should be less than available amount.", getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {
                        }
                    });
                    return;
                }
                if (comparisionSymbol.equalsIgnoreCase(symbol)) {
                    if (desfinalAmountET.getText().toString().length() == 0) {
                        alertDialogs.alertDialog(WithdrawalFundScreen.this, getResources().getString(R.string.Response), "Enter Destination tag", getResources().getString(R.string.ok), "", new DialogCallBacks() {
                            @Override
                            public void getDialogEvent(String buttonPressed) {
                            }
                        });
                        return;
                    }
                }

                Intent intent = new Intent(WithdrawalFundScreen.this, ConfirmWithdrawalFundScreen.class);
                intent.putExtra(DefaultConstants.destinationAddressET, destinationAddressET.getText().toString());
                intent.putExtra(DefaultConstants.currenyAmount, BTCAmountET.getText().toString());
                intent.putExtra(DefaultConstants.feeapplicable, fee + "");
                intent.putExtra(DefaultConstants.total, finalAmountET.getText().toString());
                intent.putExtra(DefaultConstants.remarks, remarksET.getText().toString());
                intent.putExtra(DefaultConstants.symbol, symbol);
                intent.putExtra(DefaultConstants.destinationtag, desfinalAmountET.getText().toString());
                startActivityForResult(intent, 1003);


            }
        });

        scanImgIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanQrCode();
            }
        });

        BTCAmountET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 0) {
                    if (s.toString().startsWith(".") && s.toString().length() == 1) {
                        return;
                    }

                    if (fee_type.equalsIgnoreCase("fixed")) {
                        double total = Double.parseDouble(s.toString()) - Double.parseDouble(fee + "");
                        if (total > 0) {

                            finalAmountET.setText(formatter.format(total) + "");
                        } else {
                            finalAmountET.setText("0");
                        }
                    } else {
                        double total = Double.parseDouble(s.toString()) * fee / 100;
                        total = Double.parseDouble(s.toString()) - total;
                        if (total > 0) {

                            finalAmountET.setText(formatter.format(total) + "");
                        } else {
                            finalAmountET.setText("0");
                        }
                    }
                } else {
                    finalAmountET.setText("0");
                }
            }
        });

    }


    private void scanQrCode() {
        Intent intent = new Intent(this, ScanQrCode.class);
        intent.putExtra("title", "Scan Qr code");
        startActivityForResult(intent, 1001);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            if (data != null) {
                String qrcode = data.getStringExtra("code");
                destinationAddressET.setText(qrcode);
            }
        } else if (requestCode == 1002) {
            if (data != null) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                intent.putExtra("data", "data");
                finish();
            }

        } else if (requestCode == 1003) {
            if (data != null) {
                finish();
            }
        }
    }


}

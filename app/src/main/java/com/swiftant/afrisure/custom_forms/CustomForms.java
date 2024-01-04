package com.swiftant.afrisure.custom_forms;

import static com.swiftant.afrisure.common.FlowDetails.FLOW_DETAILS;
import static com.swiftant.afrisure.common.Services.checkNull;
import static com.swiftant.afrisure.common.Services.token;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.swiftant.afrisure.GetPremium.GetPremium;
import com.swiftant.afrisure.R;
import com.swiftant.afrisure.common.FlowDetails;
import com.swiftant.afrisure.common.Services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class CustomForms extends AppCompatActivity {
    Services services;
    RecyclerView dynamicFormView;
    LinearLayout nextBtn;
    List<FormField> formFields;

    FlowDetails flowDetails;
    TextView lineOfBusinessTv, productsTitleTv, productTitleTv;

    LinearLayout detailsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_forms);

        try {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(R.drawable.back_ic);
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                getSupportActionBar().setTitle("Custom Forms");
            }
            getFlowDetails();
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        services = new Services(this, null);
        init();
    }

    private void assignValues() {
        try {
            if (flowDetails != null) {
                detailsLayout.setVisibility(View.VISIBLE);
                lineOfBusinessTv.setText(flowDetails.lineOfBusiness);
                if (flowDetails.products.size() == 1) {
                    productsTitleTv.setVisibility(View.GONE);
                    productTitleTv.setVisibility(View.VISIBLE);
                    productTitleTv.setText(flowDetails.products.get(0));
                } else {
                    productTitleTv.setVisibility(View.GONE);
                    productsTitleTv.setVisibility(View.VISIBLE);
                    productsTitleTv.setText(flowDetails.products.get(0));
                    productsTitleTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            productsPopup();
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getFlowDetails() {
        try {
            Intent intent = getIntent();
            flowDetails = (FlowDetails) intent.getSerializableExtra(FLOW_DETAILS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void productsPopup() {
        try {
// Create an AlertDialog Builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
// Create an ArrayAdapter for the ListView
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, flowDetails.products);
            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
// Create and show the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void init() {
        try {
            dynamicFormView = findViewById(R.id.dynamicFormView);
            nextBtn = findViewById(R.id.nextBtn);
            detailsLayout = findViewById(R.id.detailsLayout);
            lineOfBusinessTv = findViewById(R.id.line_of_business_tv);
            productsTitleTv = findViewById(R.id.productsTextView);
            productTitleTv = findViewById(R.id.productTextView);

            assignValues();

            services.showDialog();
            getProducts();
            nextBtn.setOnClickListener(onClickNext -> {
                for (FormField eachFormField : formFields) {
                    if (eachFormField.isRequired) {
                        String value = eachFormField.getValue();
                        System.out.println(eachFormField.getLabel());
                        System.out.println(value + "\n\n\n\n");
                        if (value.isEmpty()) {
                            Toast.makeText(this, "Please " + (eachFormField.getType().equals("select") ? "select " : "enter ") + eachFormField.getLabel() + " to continue!", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (eachFormField.regexPattern != null && !eachFormField.regexPattern.isEmpty() && !eachFormField.validationMsg.isEmpty()) {
                            Toast.makeText(this, eachFormField.validationMsg, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

                //TODO Redirect to the next api.
                Intent intent = new Intent(this, GetPremium.class);
                intent.putExtra("getPremiumBody", getAnswerValues().toString());
                intent.putExtra(FLOW_DETAILS, flowDetails);
                startActivity(intent);
            });
        } catch (Exception e) {
            services.handleException(e);
        }
    }

    private JsonObject getAnswerValues() {
        try {
            JsonObject answerValues = new JsonObject();
            for (FormField eachFormField : formFields) {
                answerValues.addProperty(eachFormField.getKey(), eachFormField.getValue());
            }

            answerValues.add("productIDs", new Gson().toJsonTree((flowDetails.productsId)));
            answerValues.addProperty("quotationSearchID", flowDetails.quotationSearchID);
            answerValues.addProperty("quotationRefID", flowDetails.quotationRefId);
            return answerValues;
        } catch (Exception e) {
            e.printStackTrace();
            services.handleException(e);
        }
        return new JsonObject();
    }

    private int[] convertStringArrayToInt(String[] stringArray) {
        int length = stringArray.length;
        int[] intArray = new int[length];

        for (int i = 0; i < length; i++) {
            // Use Integer.parseInt to convert each element of stringArray to an int
            intArray[i] = Integer.parseInt(stringArray[i]);
        }

        return intArray;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        getOnBackPressedDispatcher().onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void getProducts() {
        try {
            if (services.isNetworkConnected()) {
                if (services.checkGpsStatus()) {
                    new Thread(() -> {
                        try {
                            String baseUrl = services.baseUrl + "api/digital/core/PremiumLogic/GetProducts";
                            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(120, TimeUnit.SECONDS).writeTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS).build();
                            final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                            JsonObject details = new JsonObject();
                            details.add("productIDs", null);
                            details.addProperty("quotationSearchID", flowDetails.quotationSearchID);
                            details.add("premiumCategoryIDs", new Gson().toJsonTree(Arrays.asList()));

                            String insertString = details.toString();


                            RequestBody body = RequestBody.create(insertString, JSON);
                            Request request = new Request.Builder().url(baseUrl).header("Authorization", "Bearer " + token).post(body).build();

                            Response staticResponse;

                            try {
                                staticResponse = client.newCall(request).execute();

                                if (staticResponse.body() != null) {
                                    if (staticResponse.code() == 401) {
                                        //TODO HANDLE UN AUTH
                                    } else {
                                        String responseString = staticResponse.body().string();
                                        JSONObject staticResObj = new JSONObject(responseString);
                                        String rCode = staticResObj.optString("rcode");
                                        if (checkNull(rCode)) {
                                            if (rCode.equals("401")) {
                                                services.dismissDialog();
                                                //TODO HANDLE UN AUTH
                                            } else if (rCode.equals("200")) {
                                                formFields = extractFormFieldsFromResponse(responseString);
                                                runOnUiThread(() -> {
                                                    DynamicFormAdapter dynamicFormAdapter = new DynamicFormAdapter(CustomForms.this, formFields);
                                                    dynamicFormView.setLayoutManager(new LinearLayoutManager(CustomForms.this));
                                                    dynamicFormView.setAdapter(dynamicFormAdapter);
                                                });
                                                services.dismissDialog();
                                            } else {
                                                services.dismissDialog();
                                                JSONArray rMsg = staticResObj.getJSONArray("rmsg");
                                                services.showErrorMessageFromAPI(rMsg);
                                            }
                                        } else {
                                            stopLoading();
                                            services.somethingWentWrong();
                                        }
                                    }
                                } else {
                                    stopLoading();
                                    services.somethingWentWrong();
                                }


                            } catch (Exception e) {
                                services.dismissDialog();
                                services.handleException(e);
                            }
                        } catch (Exception e) {
                            services.dismissDialog();
                            services.handleException(e);
                        }
                    }).start();
                } else {
                    services.redirectToGpsSettings();
                    stopLoading();
                }
            } else {
                services.dismissDialog();
                //TODO HANDLE NETWORK
            }
        } catch (Exception e) {
            services.dismissDialog();
            services.handleException(e);
        }
    }

    public static List<FormField> extractFormFieldsFromResponse(String jsonResponse) {
        List<FormField> formFields = new ArrayList<>();

        try {
            JSONObject response = new JSONObject(jsonResponse);
            JSONArray allApiFormally = response.getJSONObject("rObj").getJSONArray("getAllAPIFormaly");

            // Assuming we're only interested in the first element of the getAllAPIFormally array
            JSONArray fieldGroups = allApiFormally.getJSONObject(0).getJSONArray("fieldGroup");

            for (int i = 0; i < fieldGroups.length(); i++) {
                JSONObject fieldGroup = fieldGroups.getJSONObject(i);
                JSONObject templateOptions = fieldGroup.getJSONObject("templateOptions");

                String key = fieldGroup.optString("key", "");
                String type = fieldGroup.optString("type", "");
                String label = templateOptions.optString("label", "");
                boolean isRequired = templateOptions.optBoolean("required", false);
                String placeholder = templateOptions.optString("placeholder", "");
                String templateType = templateOptions.optString("type", "");
                boolean isSelectAllEnabled = templateOptions.optBoolean("selectAllOption", false);
                boolean isMultiple = templateOptions.optBoolean("multiple", false);
                String regexPattern = templateOptions.optString("pattern", "");
                String validationMsg = "";
                try {
                    validationMsg = templateOptions.getJSONObject("validation").getJSONObject("messages").optString("required", "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int orderData = fieldGroup.optInt("orderData");
                JSONArray optionsArray = templateOptions.optJSONArray("options");
                List<FormField.Options> options = new ArrayList<>();

                if (optionsArray != null) {
                    for (int j = 0; j < optionsArray.length(); j++) {
                        JSONObject eachOptionObj = optionsArray.getJSONObject(j);
                        FormField.Options eachOption = new FormField.Options();
                        eachOption.label = eachOptionObj.optString("label", "");
                        eachOption.id = eachOptionObj.optString("value", "");
                        eachOption.isSelected = false;
                        options.add(eachOption);
                    }
                }

                if (!checkNull(placeholder)) {
                    placeholder = "";
                }


                if (isRequired && ((type.toLowerCase(Locale.ROOT).equals("select") && !options.isEmpty()) || type.toLowerCase(Locale.ROOT).equals("input"))) {
                    FormField formField = new FormField(key, label, type, templateType, orderData, isMultiple, isSelectAllEnabled, placeholder, options);
                    formField.isRequired = true;
                    formField.regexPattern = regexPattern;
                    formField.validationMsg = validationMsg;
                    formFields.add(formField);
                }
            }
            formFields.sort(Comparator.comparingInt(FormField::getOrder));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return formFields;
    }

    private void stopLoading() {
        services.dismissDialog();
    }

}
package com.example.food_product_comparison_android_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.JsonReader;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.food_product_comparison_android_app.Dialogs.LoadingDialog;
import com.example.food_product_comparison_android_app.GeneralJavaClasses.NutritionAttribute;
import com.example.food_product_comparison_android_app.GeneralJavaClasses.Product;
import com.example.food_product_comparison_android_app.GeneralJavaClasses.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.PasswordAuthentication;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Utils {
    // This class is for general, common data that is being shared among multiple units
    public static final int CAMERA_REQUEST_CODE = 814736521;
    public static final int NUTRITION_TABLE_PIC_REQUEST = 57219432;
    public static final int PRODUCT_LOOK_PIC_REQUEST = 307188697;
    public static final int RC_SIGN_IN = 900914;
    public static final int LOCAL_LOGIN = 1;
    public static final int FACEBOOK_LOGIN = 2;
    public static final int GOOGLE_LOGIN = 3;
    public static final int MAX_LEN_USERNAME = 10;
    public static final int MAX_LEN_FIRSTNAME = 25;
    public static final int MAX_LEN_LASTNAME = 25;
    public static final int MAX_LEN_EMAIL = 45;
    public static final int MAX_LEN_PASSWORD = 20;
    public static final String APP_LOCAL_SP = "APP LOCAL SHARED PREFERENCES";
    public static final String LOGGED_USER = "LOGGED USER";
    public static final int MAX_SERVER_RESPOND_SEC = 3;
    public static final String PRODUCT_TRANSFER_TAG = "PRODUCT_TRANSFER_TAG";
    public static final String PRODUCT_BARCODE_TRANSFER_TAG = "PRODUCT_BARCODE_TRANSFER_TAG";
    public static final String LOADING_BAR_TAG = "LOADING_BAR_TAG";
    public static final DateFormat DATE_FORMAT_DISPLAYED = new SimpleDateFormat("EEE, dd-MMM-yyyy", Locale.ENGLISH);
    public static final String PRODUCT_PRICE = "price";
    public static final int FEEDBACK_CONTRIBUTION_POINTS = 5;
    public static final int PRODUCT_CONTRIBUTION_POINTS = 10;
    public static final int PRODUCT_REVIEW_CONTRIBUTION_POINTS = 5;

    // User Input Validation
    public static final String REGEX_CONTAIN_LOWERCASE = "^.*[a-z].*$";
    public static final int EMAIL_INPUT = 0;
    public static final int USERNAME_INPUT = 1;
    public static final int PASSWORD_INPUT = 2;
    public static final int FIRSTNAME_INPUT = 3;
    public static final int LASTNAME_INPUT = 4;
    public static final int MIN_PASSWORD_LENGTH = 8;

    // Animations
    public static final int login_view_animation_translation = 300;
    public static final int login_view_animation_duration = 600;

    public static String validateUserInfoInput(Context context, String input, int input_type)
    {
        if (input.isEmpty())
            return context.getString(R.string.error_input_cannot_be_empty);

        String regexPattern;

        switch (input_type)
        {
            case EMAIL_INPUT:
                regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_.-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
                if (!patternMatches(input, regexPattern))
                    return context.getString(R.string.email_input_error);
                else
                    break;
            case USERNAME_INPUT:
                regexPattern = "^[a-zA-Z0-9]{1,10}$";
                if (!patternMatches(input, regexPattern))
                    return context.getString(R.string.username_input_error);
                else
                    break;
            case PASSWORD_INPUT:
                regexPattern = "^(?=.*[a-z])(?=.*[A-Z]).{8,20}$";
                if (!patternMatches(input, regexPattern))
                {
                    if (input.length() < 8) {
                        return String.format(context.getString(R.string.password_input_too_short_error), MIN_PASSWORD_LENGTH);
                    } else if (!patternMatches(input, REGEX_CONTAIN_LOWERCASE))
                    {
                        return context.getString(R.string.password_input_no_lowercase_error);
                    }
                    else
                    {
                        return context.getString(R.string.password_input_no_uppercase_error);
                    }
                }
                else
                    break;
            case FIRSTNAME_INPUT:
            case LASTNAME_INPUT:
                regexPattern = "^[a-zA-Z]*$";
                if (!patternMatches(input, regexPattern))
                    return context.getString(R.string.flname_input_error);
                else
                    break;
            default:
                break;
        }

        return context.getString(R.string.valid_user_input);
    }

    public static boolean patternMatches(String input, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(input)
                .matches();
    }

    public static String getAlphaNumericRandomString(int str_len)
    {
        String alphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(str_len);

        for (int i = 0; i < str_len; i++) {

            // generate a random number between
            // 0 to AlphaNumericString.length()
            int index
                    = (int)(alphaNumericString.length()
                    * Math.random());

            // add the char at index in alphaNumericString to sb
            sb.append(alphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    public static void sendPasswordResetEmailToTargetAddress(Context context, String email_address, String new_password)
    {
        // email_address must be guaranteed to be valid before executing this method.

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler uiHandler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            final String sender = context.getString(R.string.app_email_address);
            final String sender_password = context.getString(R.string.app_email_address_password);
            String email_msg = String.format(context.getString(R.string.password_reset_email_content), new_password);

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new javax.mail.Authenticator(){
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(sender, sender_password);
                }
            });

            try{
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress((sender)));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email_address));
                message.setSubject(context.getString(R.string.password_reset_email_subejct));
                message.setText(email_msg);
                Transport.send(message);
            } catch(MessagingException e) {
                uiHandler.post(() -> {
                    Toast.makeText(context, "Messaging Exception:" + e, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    public static User getLoggedUser(Context context)
    {
        Gson gson = new Gson();
        Type type = new TypeToken<User>() {}.getType();
        User user =  gson.fromJson(context.getSharedPreferences(Utils.APP_LOCAL_SP, 0)
                .getString(Utils.LOGGED_USER, null), type);

        return user != null ? user : new User();
    }

    public static void updateUserLoginStatus(Context context, User user)
    {
        SharedPreferences sp = context.getSharedPreferences(Utils.APP_LOCAL_SP, 0);
        SharedPreferences.Editor sp_editor = sp.edit();

        sp_editor.putString(Utils.LOGGED_USER, new Gson().toJson(user));

        sp_editor.apply();
    }

    public static void removeUserLoginStatus(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(Utils.APP_LOCAL_SP, 0);
        SharedPreferences.Editor sp_editor = sp.edit();

        sp_editor.remove(Utils.LOGGED_USER);

        sp_editor.apply();
    }

    public static Retrofit getRetrofit(Context context)
    {
        return new Retrofit.Builder()
                .baseUrl(context.getString(R.string.server_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ServerRetrofitAPI getServerAPI(Context context)
    {
        return getRetrofit(context).create(ServerRetrofitAPI.class);
    }

    public static void displayWelcomeToast(Context context, String firstname, String lastname)
    {
        Toast.makeText(context, String.format(context.getString(R.string.welcome_to_scannera), firstname + " " + lastname), Toast.LENGTH_LONG).show();
    }

    public static void updateUserPasswordAndActivity(Long init_time, Context context, String email_address, String userId)
    {
        LoadingDialog loading_dialog = new LoadingDialog(context);
        try {
            loading_dialog.show();
        } catch (Exception e) {
            loading_dialog.dismiss();
        }

        // Randomly generate a new password of length Utils.MIN_PASSWORD_LENGTH
        String new_password = Utils.getAlphaNumericRandomString(Utils.MIN_PASSWORD_LENGTH);

        Call<Void> call = Utils.getServerAPI(context).updateUserPasswordById(userId, new_password);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                loading_dialog.dismiss();

                if(response.isSuccessful())
                {
                    Utils.sendPasswordResetEmailToTargetAddress(context, email_address, new_password);

                    // Navigate to the email sent activity once sent
                    ((Activity) context).finish();
                    Intent intent = new Intent(context, PasswordEmailSentActivity.class);
                    intent.putExtra(ForgottenPasswordActivity.RESET_EMAIl_ADDRESS_KEY, email_address);
                    intent.putExtra(ForgottenPasswordActivity.RESET_USER_ID, userId);
                    context.startActivity(intent);
                }
                else
                {
                    if ((System.currentTimeMillis() - init_time) / 1000 < Utils.MAX_SERVER_RESPOND_SEC) {
                        updateUserPasswordAndActivity(init_time, context, email_address, userId);
                        Log.e("DEBUG", "Update Password Response code: " + response.code());
                    }
                    else {
                        Toast.makeText(context, context.getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                loading_dialog.dismiss();
                Toast.makeText(context, context.getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    public static ArrayList<Object> parseProductsFromResponse(Context context, ResponseBody responseBody) {
        ArrayList<Object> items = new ArrayList<>();
        Date prev_scan_date = null;

        try {
            InputStream responseBodyIS = responseBody.byteStream();
            InputStreamReader responseBodyReader = new InputStreamReader(responseBodyIS, StandardCharsets.UTF_8);
            JsonReader jsonReader = new JsonReader(responseBodyReader);
            String keyName;

            jsonReader.beginArray();
            while (jsonReader.hasNext()) {
                // Parse a single product
                Product product = new Product();

                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    keyName = jsonReader.nextName();

                    switch(keyName) {
                        case ServerRetrofitAPI.PRODUCT_ID_SERVER:
                            product.setProductId(jsonReader.nextString());
                            break;
                        case ServerRetrofitAPI.PRODUCT_BARCODE_SERVER:
                            product.setBarcode(jsonReader.nextString());
                            break;
                        case ServerRetrofitAPI.PRODUCT_BRAND_SERVER:
                            product.setBrand(jsonReader.nextString());
                            break;
                        case ServerRetrofitAPI.PRODUCT_NAME_SERVER:
                            product.setName(jsonReader.nextString());
                            break;
                        case ServerRetrofitAPI.PRODUCT_PRICE_SERVER:
                            product.setPrice(Float.parseFloat(jsonReader.nextString()));
                            break;
                        case ServerRetrofitAPI.PRODUCT_CATEGORY_SERVER:
                            product.setCategory(jsonReader.nextString());
                            break;
                        case ServerRetrofitAPI.PRODUCT_NUTRITION_SERVER:
                            product.setNutritionAttributes(parseProductNutritionFromJSON(context, jsonReader.nextString()));
                            break;
                        case ServerRetrofitAPI.PRODUCT_IS_STARRED_SERVER:
                            product.setStarred(jsonReader.nextBoolean());
                            break;
                        case ServerRetrofitAPI.PRODUCT_SCAN_DATE_SERVER:
                            Date date = ServerRetrofitAPI.DATE_FORMAT_SERVER.parse(jsonReader.nextString());

                            if(date != null && (prev_scan_date == null || Objects.requireNonNull(date).compareTo(prev_scan_date) != 0))
                            {
                                prev_scan_date = date;
                                items.add(Utils.DATE_FORMAT_DISPLAYED.format(date));
                            }

                            break;
                        default:
                            jsonReader.skipValue();
                            break;
                    }
                }

                items.add(product);
                jsonReader.endObject();
            }

            jsonReader.endArray();
            responseBody.close();
        } catch (IOException | ParseException e) {
            Toast.makeText(context, context.getString(R.string.general_error), Toast.LENGTH_LONG).show();
            Log.e("DEBUG", "IOException|ParseException - parse products: " + e);
        }

        return items;
    }

    public static Product parseASingleProductFromResponse(Context context, ResponseBody responseBody)
    {
        // ************************************
        // IT IS GUARANTEED THERE EXISTS ONE PRODUCT IN THE RESPONSE
        // WHEN CALLING THIS METHOD.
        // ************************************

        Product product = new Product();

        try{
            InputStream responseBodyIS = responseBody.byteStream();
            InputStreamReader responseBodyReader = new InputStreamReader(responseBodyIS, StandardCharsets.UTF_8);
            JsonReader jsonReader = new JsonReader(responseBodyReader);
            String keyName;

            jsonReader.beginObject();

            while (jsonReader.hasNext()) {
                keyName = jsonReader.nextName();

                switch(keyName) {
                    case ServerRetrofitAPI.PRODUCT_ID_SERVER:
                        product.setProductId(jsonReader.nextString());
                        break;
                    case ServerRetrofitAPI.PRODUCT_BARCODE_SERVER:
                        product.setBarcode(jsonReader.nextString());
                        break;
                    case ServerRetrofitAPI.PRODUCT_BRAND_SERVER:
                        product.setBrand(jsonReader.nextString());
                        break;
                    case ServerRetrofitAPI.PRODUCT_NAME_SERVER:
                        product.setName(jsonReader.nextString());
                        break;
                    case ServerRetrofitAPI.PRODUCT_PRICE_SERVER:
                        product.setPrice(Float.parseFloat(jsonReader.nextString()));
                        break;
                    case ServerRetrofitAPI.PRODUCT_CATEGORY_SERVER:
                        product.setCategory(jsonReader.nextString());
                        break;
                    case ServerRetrofitAPI.PRODUCT_NUTRITION_SERVER:
                        product.setNutritionAttributes(parseProductNutritionFromJSON(context, jsonReader.nextString()));
                        break;
                    case ServerRetrofitAPI.PRODUCT_IS_STARRED_SERVER:
                        product.setStarred(jsonReader.nextBoolean());
                        break;
                    default:
                        jsonReader.skipValue();
                        break;
                }
            }

            jsonReader.endObject();
            responseBody.close();
        }catch (IOException e) {
            Toast.makeText(context, context.getString(R.string.general_error), Toast.LENGTH_LONG).show();
            Log.e("DEBUG", "IOException - parse a single product: " + e);
        }

        return product;
    }

    public static HashMap<String, NutritionAttribute> parseProductNutritionFromJSON(Context context, String product_nutrition)
    {
        HashMap<String, NutritionAttribute> nutritionAttributes = new HashMap<>();

        try
        {
            JSONObject nutrition = new JSONObject(product_nutrition);
            JSONArray keys = nutrition.names();

            if (keys != null)
            {
                for(int i = 0; i < keys.length(); i++)
                {
                    String name = keys.getString(i).toLowerCase();
                    JSONObject data = nutrition.getJSONObject(keys.getString(i));

                    nutritionAttributes.put(name, new NutritionAttribute(name, Float.parseFloat(data.getString("value")), data.getString("unit")));
                }
            }

        } catch(JSONException e) {
            Toast.makeText(context, context.getString(R.string.parsing_error), Toast.LENGTH_LONG).show();
        }

        return nutritionAttributes;
    }

    public static void toggleProductStar(Long init_time, Activity activityContext, Context appContext, ImageButton star_btn, Product product)
    {
        LoadingDialog loading_dialog = new LoadingDialog(activityContext);
        try {
            loading_dialog.show();
        } catch (Exception e) {
            loading_dialog.dismiss();
        }

        Call<Void> call = product.getStarred()? Utils.getServerAPI(activityContext).
                unStarProduct(Utils.getLoggedUser(activityContext).getId(), product.getProductId())
                : Utils.getServerAPI(activityContext).starProduct(Utils.getLoggedUser(activityContext).getId(), product.getProductId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                loading_dialog.dismiss();

                if (response.isSuccessful())
                {
                    if(product.getStarred())
                    {
                        star_btn.setImageDrawable(ContextCompat.getDrawable(appContext, android.R.drawable.btn_star_big_off));
                    }
                    else
                    {
                        star_btn.setImageDrawable(ContextCompat.getDrawable(appContext, android.R.drawable.btn_star_big_on));
                    }

                    product.setStarred(!product.getStarred());
                }
                else
                {
                    if ((System.currentTimeMillis() - init_time) / 1000 < Utils.MAX_SERVER_RESPOND_SEC) {
                        toggleProductStar(init_time, activityContext, appContext, star_btn, product);
                        Log.e("DEBUG", "Toggle product star response code: " + response.code());
                    }
                    else if(response.code() == 405)
                    {
                        Toast.makeText(activityContext, activityContext.getString(R.string.general_error), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(activityContext, activityContext.getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                loading_dialog.dismiss();
                Toast.makeText(activityContext, activityContext.getString(R.string.general_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void navigateToProductInfoActivity(Context context, Product product)
    {
        Intent intent = new Intent(context, ProductInformationActivity.class);
        intent.putExtra(Utils.PRODUCT_TRANSFER_TAG, new Gson().toJson(product));
        context.startActivity(intent);
    }
}

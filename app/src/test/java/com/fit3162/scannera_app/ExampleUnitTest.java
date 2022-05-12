package com.fit3162.scannera_app;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.fit3162.scannera_app.GeneralJavaClasses.NutritionAttribute;
import com.fit3162.scannera_app.GeneralJavaClasses.User;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Objects;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {
    private User sampleUser;

    @Mock
    private Context mockContext;

    @Mock
    private SharedPreferences mockSharedPreferences;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockContext = mock(Context.class);

        sampleUser = new User(0, "testuser", "test", "user", "testuser@gmail.com", "Testuser123", "http://sampleurl.com", 0);
        sampleUser.setId("123456789");
        when(mockContext.getSharedPreferences(Utils.APP_LOCAL_SP, 0)).thenReturn(mockSharedPreferences);
        when(mockSharedPreferences.getString(Utils.LOGGED_USER, null)).thenReturn(new Gson().toJson(sampleUser));
        when(mockContext.getString(R.string.email_input_error)).thenReturn("Invalid email address format, please check again.");
        when(mockContext.getString(R.string.username_input_error)).thenReturn("Sorry, username should not contain any special characters (e.g., !@#$%^&amp;*).");
        when(mockContext.getString(R.string.password_input_too_short_error)).thenReturn("Sorry, password must contain at least %1$d characters.");
        when(mockContext.getString(R.string.password_input_no_uppercase_error)).
                thenReturn("Sorry, password must contain at least one uppercase character.");
        when(mockContext.getString(R.string.flname_input_error)).thenReturn("Sorry, this field must only contain English characters.");
        when(mockContext.getString(R.string.valid_user_input)).thenReturn("success");
    }

    @Test
    public void regexMatcherTest_mismatchShouldIndicateFalse() {
        assertFalse(Utils.patternMatches("abc#d32", "^[a-zA-Z0-9]{1,10}$"));
    }

    @Test
    public void regexMatcherTest_matchShouldIndicateTrue() {
        assertTrue(Utils.patternMatches("aAbcd123", "^[a-zA-Z0-9]{1,10}$"));
    }

    @Test
    public void emailValidation_doubleAtShouldIndicateIncorrect() {
        assertNotEquals(mockContext.getString(R.string.valid_user_input),
                Utils.validateUserInfoInput(mockContext, "test@user@qq.com", Utils.EMAIL_INPUT));
    }

    @Test
    public void emailValidation_invalidSuffixShouldIndicateIncorrect() {
        assertNotEquals(mockContext.getString(R.string.valid_user_input),
                Utils.validateUserInfoInput(mockContext, "testuser@monash", Utils.EMAIL_INPUT));
    }

    @Test
    public void emailValidation_validEmailAddressShouldIndicateCorrect() {
        assertEquals(mockContext.getString(R.string.valid_user_input),
                Utils.validateUserInfoInput(mockContext, "testuser@gmail.com", Utils.EMAIL_INPUT));
    }

    @Test
    public void usernameValidation_specialCharShouldIndicateIncorrect() {
        assertNotEquals(mockContext.getString(R.string.valid_user_input),
                Utils.validateUserInfoInput(mockContext, "testU@User", Utils.USERNAME_INPUT));
    }

    @Test
    public void usernameValidation_validUsernameShouldIndicateCorrect() {
        assertEquals(mockContext.getString(R.string.valid_user_input),
                Utils.validateUserInfoInput(mockContext, "testUser12", Utils.USERNAME_INPUT));
    }

    @Test
    public void passwordValidation_TooShortShouldIndicateIncorrect() {
        assertNotEquals(mockContext.getString(R.string.valid_user_input),
                Utils.validateUserInfoInput(mockContext, "testPas", Utils.PASSWORD_INPUT));
    }

    @Test
    public void passwordValidation_noUpperShouldIndicateIncorrect() {
        assertNotEquals(mockContext.getString(R.string.valid_user_input),
                Utils.validateUserInfoInput(mockContext, "testpassword", Utils.PASSWORD_INPUT));
    }

    @Test
    public void passwordValidation_validPasswordShouldIndicateCorrect() {
        assertEquals(mockContext.getString(R.string.valid_user_input),
                Utils.validateUserInfoInput(mockContext, "testPassword", Utils.PASSWORD_INPUT));
    }

    @Test
    public void flNameValidation_specialCharFLNameShouldIndicateIncorrect() {
        assertNotEquals(mockContext.getString(R.string.valid_user_input),
                Utils.validateUserInfoInput(mockContext, "Test!", Utils.FIRSTNAME_INPUT));
    }

    @Test
    public void flNameValidation_validFLNameShouldIndicateCorrect() {
        assertEquals(mockContext.getString(R.string.valid_user_input),
                Utils.validateUserInfoInput(mockContext, "User", Utils.LASTNAME_INPUT));
    }

    @Test
    public void UserDataLocalSPTest_getUserData() {
        assertEquals(new Gson().toJson(sampleUser), new Gson().toJson(Utils.getLoggedUser(mockContext)));
    }
}
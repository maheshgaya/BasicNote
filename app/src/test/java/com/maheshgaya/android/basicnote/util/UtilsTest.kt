package com.maheshgaya.android.basicnote.util

import org.junit.Assert
import org.junit.Test

/**
 * Created by Mahesh Gaya on 8/12/17.
 */
class UtilsTest {
    @Test
    @Throws(Exception::class)
    fun test_toCamelCase() {
        Assert.assertTrue("Check trimming element 1", " i am happy".toCamelCase() == " I Am Happy")
        Assert.assertTrue("Check trimming element 2", " i am happy ".toCamelCase() == " I Am Happy ")
        Assert.assertTrue("Check no trimming space", "i am happy".toCamelCase() == "I Am Happy")
        Assert.assertTrue("Check no space", "iamhappy".toCamelCase() == "Iamhappy")
        Assert.assertTrue("Check punctuation 1", "what is that?".toCamelCase() == "What Is That?")
        Assert.assertTrue("Check punctuation 2", " ? = ! \" \'".toCamelCase() == " ? = ! \" \'")
    }

    @Test
    @Throws(Exception::class)
    fun test_validateEmail(){
        Assert.assertTrue("Check has @ and .", "test.email@gmail.com".validateEmail())
        Assert.assertTrue("Check only has @", "test.email@gmailcom".validateEmail())
        Assert.assertFalse("Check not @", "test.email.gmail.com".validateEmail())
        Assert.assertFalse("Check no @ and no .", "testemailgmailcom".validateEmail())
        Assert.assertFalse("Check no characters", "@.".validateEmail())
        Assert.assertFalse("Check no characters", "@".validateEmail())
        Assert.assertFalse("Check no characters", "test.email".validateEmail())

    }

    @Test
    @Throws(Exception::class)
    fun test_validatePassword(){
        Assert.assertFalse("Check length less than 8", "testPa".validatePassword())
        Assert.assertTrue("Check length equal to 8", "testPass".validatePassword())
        Assert.assertTrue("Check length more than 8", "testPassword".validatePassword())

        Assert.assertFalse("Check invalid characters", "`".validatePassword())
        Assert.assertFalse("Check invalid characters", "~".validatePassword())
        Assert.assertFalse("Check invalid characters", ".".validatePassword())
        Assert.assertFalse("Check invalid characters", "(".validatePassword())
        Assert.assertFalse("Check invalid characters", ")".validatePassword())
        Assert.assertFalse("Check invalid characters", "*".validatePassword())
        Assert.assertFalse("Check invalid characters", "^".validatePassword())
        Assert.assertFalse("Check invalid characters", "+".validatePassword())
        Assert.assertFalse("Check invalid characters", "-".validatePassword())
        Assert.assertFalse("Check invalid characters", "%".validatePassword())
        Assert.assertFalse("Check invalid characters", "[".validatePassword())
        Assert.assertFalse("Check invalid characters", "]".validatePassword())

        Assert.assertTrue("Check valid characters", "=abcdefgh".validatePassword())
        Assert.assertTrue("Check valid characters", "&abcdefgh".validatePassword())
        Assert.assertTrue("Check valid characters", "_abcdefgh".validatePassword())
        Assert.assertTrue("Check valid characters", "#abcdefgh".validatePassword())
        Assert.assertTrue("Check valid characters", "!abcdefgh".validatePassword())
        Assert.assertTrue("Check valid characters", "&abcdefgh".validatePassword())
    }
}
package ua.com.vladyslav.spribe.listeners;

import io.qameta.allure.testng.AllureTestNg;
import org.testng.IAlterSuiteListener;
import org.testng.ISuiteListener;

public class AllureTestListener extends AllureTestNg implements ISuiteListener, IAlterSuiteListener {
    // Allure integration handled by plugin and RestAssured filter
    // if you need to do something here do not forget to add this listener to testng.xml
    //         <listener class-name="ua.com.vladyslav.spribe.listeners.AllureTestListener"/>
}

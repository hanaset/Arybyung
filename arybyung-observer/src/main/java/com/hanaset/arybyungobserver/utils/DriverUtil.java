package com.hanaset.arybyungobserver.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class DriverUtil {

    public static void clipboardCopy(String data, String xpath, WebDriver driver) throws InterruptedException {

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(data), null);
        Actions action = new Actions(driver);

        driver.findElement(By.xpath(xpath)).click();

        Thread.sleep(1000);

        action.keyDown(Keys.COMMAND).sendKeys("v").keyUp(Keys.COMMAND).build().perform();
    }
}

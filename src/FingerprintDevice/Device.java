/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FingerprintDevice;

import Forms.ScanFinger;
import com.zkteco.biometric.FingerprintSensorErrorCode;
import com.zkteco.biometric.FingerprintSensorEx;
import java.io.IOException;

/**
 *
 * @author STN-COM-01
 */
public class Device {

    int fpWidth = 0;
    int fpHeight = 0;
    private long mhDevice = 0;
    private long mhDB = 0;
    private boolean mbStop = true;
    private int cbRegTemp = 0;
    private int iFid = 1;
    private int enroll_idx = 0;
    private byte[] imgbuf = null;
    private WorkThread workThread = null;
    private int[] templateLen = new int[1];
    private byte[] template = new byte[2048];
    private int nFakeFunOn = 1;
    private byte[][] regtemparray = new byte[3][2048];
    private byte[] lastRegTemp = new byte[2048];
    private ScanFinger scanFinger;
    private String tempMessage = "";

    /*
     * this code is for Linux Environment, comment this code otherwise.
     */
//    static {
//        System.loadLibrary("zkfp");
//    }

    public Device(ScanFinger scanFinger) {
        this.scanFinger = scanFinger;
    }

    public boolean openDevice() {
        try {
            if (0 != mhDevice) {
                //already inited
                this.scanFinger.showMessage("warning", "Please close device first!");
                return false;
            }
            int ret = FingerprintSensorErrorCode.ZKFP_ERR_OK;
            //Initialize
            cbRegTemp = 0;
            iFid = 1;
            enroll_idx = 0;
            if (FingerprintSensorErrorCode.ZKFP_ERR_OK != FingerprintSensorEx.Init()) {
                this.scanFinger.showMessage("error", "Init device failed.");
                return false;
            }
            ret = FingerprintSensorEx.GetDeviceCount();
            if (ret < 0) {
                this.scanFinger.showMessage("error", "No device connected.");
                FreeSensor();
                return false;
            }
            if (0 == (mhDevice = FingerprintSensorEx.OpenDevice(0))) {
                this.scanFinger.showMessage("error", "Open device fail, ret = " + ret + "!");
                FreeSensor();
                return false;
            }
            if (0 == (mhDB = FingerprintSensorEx.DBInit())) {
                this.scanFinger.showMessage("error", "Init DB fail, ret = " + ret + "!");
                FreeSensor();
                return false;
            }

            //For ISO/Ansi
            int nFmt = 1;	// ISO
            FingerprintSensorEx.DBSetParameter(mhDB, 5010, nFmt);

            byte[] paramValue = new byte[4];
            int[] size = new int[1];

            size[0] = 4;
            FingerprintSensorEx.GetParameters(mhDevice, 1, paramValue, size);
            fpWidth = byteArrayToInt(paramValue);
            size[0] = 4;
            FingerprintSensorEx.GetParameters(mhDevice, 2, paramValue, size);
            fpHeight = byteArrayToInt(paramValue);

            imgbuf = new byte[fpWidth * fpHeight];
            mbStop = false;
            workThread = new WorkThread();
            workThread.start();
            this.scanFinger.appendLog("Open Device Success.\nYou need scan your finger 3 times.");
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

    public void closeDevice() {
        FreeSensor();
    }

    private void FreeSensor() {
        mbStop = true;
        try {
            //wait for thread stopping
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (0 != mhDB) {
            FingerprintSensorEx.DBFree(mhDB);
            mhDB = 0;
        }
        if (0 != mhDevice) {
            FingerprintSensorEx.CloseDevice(mhDevice);
            mhDevice = 0;
        }
        FingerprintSensorEx.Terminate();
        this.scanFinger.appendLog("Success Close Device.");
    }

    public static int byteArrayToInt(byte[] bytes) {
        int number = bytes[0] & 0xFF;
        number |= ((bytes[1] << 8) & 0xFF00);
        number |= ((bytes[2] << 16) & 0xFF0000);
        number |= ((bytes[3] << 24) & 0xFF000000);
        return number;
    }

    private void OnCatpureOK(byte[] imgBuf) {
        try {
            writeBitmap(imgBuf, fpWidth, fpHeight, "fingerprint.bmp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeBitmap(byte[] imageBuf, int nWidth, int nHeight,
            String path) throws IOException {
        java.io.FileOutputStream fos = new java.io.FileOutputStream(path);
        java.io.DataOutputStream dos = new java.io.DataOutputStream(fos);

        int w = (((nWidth + 3) / 4) * 4);
        int bfType = 0x424d;
        int bfSize = 54 + 1024 + w * nHeight;
        int bfReserved1 = 0;
        int bfReserved2 = 0;
        int bfOffBits = 54 + 1024;

        dos.writeShort(bfType);
        dos.write(changeByte(bfSize), 0, 4);
        dos.write(changeByte(bfReserved1), 0, 2);
        dos.write(changeByte(bfReserved2), 0, 2);
        dos.write(changeByte(bfOffBits), 0, 4);

        int biSize = 40;
        int biWidth = nWidth;
        int biHeight = nHeight;
        int biPlanes = 1;
        int biBitcount = 8;
        int biCompression = 0;
        int biSizeImage = w * nHeight;
        int biXPelsPerMeter = 0;
        int biYPelsPerMeter = 0;
        int biClrUsed = 0;
        int biClrImportant = 0;

        dos.write(changeByte(biSize), 0, 4);
        dos.write(changeByte(biWidth), 0, 4);
        dos.write(changeByte(biHeight), 0, 4);
        dos.write(changeByte(biPlanes), 0, 2);
        dos.write(changeByte(biBitcount), 0, 2);
        dos.write(changeByte(biCompression), 0, 4);
        dos.write(changeByte(biSizeImage), 0, 4);
        dos.write(changeByte(biXPelsPerMeter), 0, 4);
        dos.write(changeByte(biYPelsPerMeter), 0, 4);
        dos.write(changeByte(biClrUsed), 0, 4);
        dos.write(changeByte(biClrImportant), 0, 4);

        for (int i = 0; i < 256; i++) {
            dos.writeByte(i);
            dos.writeByte(i);
            dos.writeByte(i);
            dos.writeByte(0);
        }

        byte[] filter = null;
        if (w > nWidth) {
            filter = new byte[w - nWidth];
        }

        for (int i = 0; i < nHeight; i++) {
            dos.write(imageBuf, (nHeight - 1 - i) * nWidth, nWidth);
            if (w > nWidth) {
                dos.write(filter, 0, w - nWidth);
            }
        }
        dos.flush();
        dos.close();
        fos.close();
    }

    public static byte[] changeByte(int data) {
        return intToByteArray(data);
    }

    public static byte[] intToByteArray(final int number) {
        byte[] abyte = new byte[4];
        abyte[0] = (byte) (0xff & number);
        abyte[1] = (byte) ((0xff00 & number) >> 8);
        abyte[2] = (byte) ((0xff0000 & number) >> 16);
        abyte[3] = (byte) ((0xff000000 & number) >> 24);
        return abyte;
    }

    private void OnExtractOK(byte[] template, int len) {
        try {
            int ret = -17;
            if (enroll_idx > 0 && FingerprintSensorEx.DBMatch(mhDB, regtemparray[enroll_idx - 1], template) <= 0) {
                tempMessage = "please press the same finger 3 times for the enrollment.";
                this.scanFinger.appendLog(tempMessage);
                return;
            }
            System.arraycopy(template, 0, regtemparray[enroll_idx], 0, 2048);
            enroll_idx++;
            if (enroll_idx == 3) {
                int[] _retLen = new int[1];
                _retLen[0] = 2048;
                byte[] regTemp = new byte[_retLen[0]];

                if (0 == (ret = FingerprintSensorEx.DBMerge(mhDB, regtemparray[0], regtemparray[1], regtemparray[2], regTemp, _retLen))
                        && 0 == (ret = FingerprintSensorEx.DBAdd(mhDB, iFid, regTemp))) {
                    iFid++;
                    cbRegTemp = _retLen[0];
                    System.arraycopy(regTemp, 0, lastRegTemp, 0, cbRegTemp);

                    //Base64 Template
                    String strBase64 = FingerprintSensorEx.BlobToBase64(regTemp, cbRegTemp);
                    if (!strBase64.isEmpty()) {
                        int templateLength = strBase64.length();
                        this.scanFinger.scannedTemplateLength = templateLength;
                        this.scanFinger.dataFinger = strBase64;
                    }
                    this.scanFinger.appendLog("Finish.");
                    this.scanFinger.displayScannedFingerprint();
                    enroll_idx = 0;
                    tempMessage = "";
                } else {
                    this.scanFinger.showMessage("error", "Scan Failed : error code " + ret);
                }
            } else {
                tempMessage = "You need to press the " + (3 - enroll_idx) + " time(s) fingerprint.";
                this.scanFinger.appendLog(tempMessage);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            this.scanFinger.showMessage("error", "Data Scanned Fingerprint not found.");
        }
    }

    private class WorkThread extends Thread {

        @Override
        public void run() {
            super.run();
            int ret = 0;
            while (!mbStop) {
                templateLen[0] = 2048;
                if (0 == (ret = FingerprintSensorEx.AcquireFingerprint(mhDevice, imgbuf, template, templateLen))) {
                    if (nFakeFunOn == 1) {
                        byte[] paramValue = new byte[4];
                        int[] size = new int[1];
                        size[0] = 4;
                        int nFakeStatus = 0;

                        //GetFakeStatus
                        ret = FingerprintSensorEx.GetParameters(mhDevice, 2004, paramValue, size);
                        nFakeStatus = byteArrayToInt(paramValue);
                        if (0 == ret && (byte) (nFakeStatus & 31) != 31) {
                            System.out.println("Is a fake finger?");
                            return;
                        }
                    }
                    OnCatpureOK(imgbuf);
                    OnExtractOK(template, templateLen[0]);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}

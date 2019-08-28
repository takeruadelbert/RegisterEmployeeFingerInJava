# Register Employee Finger Java 
re-developed 'Register Employee Finger' application using Java instead of .NET Framework thus it can cross-platform (Windows and/or Linux Environment)

create "session feature" using JSON data with encryption/decryption of DES (Data Encryption Standard) Algorithm for password.


# Install Application Demo (Ubuntu)
download link : https://drive.google.com/drive/folders/182h-i-gC9SncsrIgCuvRtgfzTApiCLcg?usp=sharing
you can install the application either via Ubuntu Software Center or via terminal:
```
sudo dpkg -i <debian package file>
```

# Uninstall
```
sudo apt-get remove fingerprint
```

# Additional Information
- If fingerprint device cannot be opened in Linux Environment, try run the application as root.
- If hyperlink feature in UI doesn't working, install gnome :
```
sudo apt-get install libgnome2-0
```
- To create dekstop application launcher as root, create this executable file "gksudo" for example.
```
pkexec env DISPLAY=$DISPLAY XAUTHORITY=$XAUTHORITY $@
```
and then put it into ```/usr/local/bin<i/>``` lastly make it executable with ``` sudo chmod 755 gksudo ```. Done!
you can execute application with privilege as root with ``` gksudo <application> ```.

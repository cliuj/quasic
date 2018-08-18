# quasic
A hash-like password generator

## * Note *
I highly recommend using this program on something like a perminantly offline computer or virtual machine.

The output will also contain ASCII characters from 33- 126. So this will only work for websites that accept these characters. A version will be created at a later date that allows exclusion of certain characters from the output.

## Main Idea:
The main idea of this program is to generate a gibberish string from any inputed keyword and prefered output size.

For example, "cat" will be used as the simple to remember keyword and the number "18" will be the output size of the gibberish string. Entering these into Quasic's respected entries will result in a gibberish string of size 18. Since the output is one-way, entering "cat and "18" will always result in the same gibberish string. This allows the generation of a strong password from simply remember something like "cat18".

## How to Use:
1. Run Launcher.java to obtain a simple GUI made from JavaFX. 

2. Enter the length of the output password(gibberish) you want and the keyword you want to use. 

3. Once both entries are entered, a gibberish password should appear on the bottom.

4. Do want every you want with the output!

## Examples of Different Outputs:
![quasic screenshot 1](https://user-images.githubusercontent.com/22797257/44302493-ca552680-a2f7-11e8-8fd4-d3f195393de6.png)

![quasic screenshot 2](https://user-images.githubusercontent.com/22797257/44302506-13a57600-a2f8-11e8-999b-9874f2293ed5.png)

![quasic screenshot 3](https://user-images.githubusercontent.com/22797257/44302508-1f913800-a2f8-11e8-9b8f-05d5269d89f6.png)


## Disclaimer

I highly recommend modifiying the uploaded(this) version to your own preference.

Using this version is (most likely) succeptible to lookup and rainbow table attacks, so use at your own risk.

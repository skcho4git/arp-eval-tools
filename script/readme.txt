README
1.14.0 (for 3.8 release)

(1) shoplist.sh
    this program can make a csv file that contains shop_id,merchant_id  of shops in a mall. 
    the parameters are: mallId isDemo, for example , 
        shoplist.sh es false (when get all shop list for Spain)
        shoplist.sh id true (when get demo shop list for Indonesia)
    when isDemo is false, the csv file name will be /input/all_shop_list.txt, which is set in merchant-tools.properties
    when isDemo is true, the csv file name will be /input/demo_shop_list.txt, which is set in merchant-tools.properties
    you can change the merchant api url settings in the shoplist.sh.
    after executing the program, you can read /logs/error.log to check whether any error happened or not.


(2) payment.sh
    this program can create payment method for shops , according to the input shop list file. 
    the parameters are: mallId isDemo isActive paymentMethodId, for example , 
        payment.sh es false true c1b12f39-64cf-e5ee-9940-3c34aa1ae212 (when add PayPal for Spain all shop, status is enabled )
        payment.sh id true false 1bda54a9-374e-494b-807b-311032edf771 (when add Kartu Debit Visa Mandiri for Indonesia demo shop, status is disabled)
    when isDemo is false, the input shop list file name will be /input/all_shop_list.txt, which is set in merchant-tools.properties
    when isDemo is true, the input shop list file name will be /input/demo_shop_list.txt, which is set in merchant-tools.properties
    you can change the merchant api url settings in payment.sh.
    after executing the program, you can read /logs/error.log to check whether any error happened or not.


(3) shipping.sh
    this program can create shipping method for shops , according to the input shop list file. 
    the parameters are: mallId isDemo isActive shippingMethodId, for example , 
        shipping.sh es true true d73cbe93-3f40-f848-778c-0e284a0de3fe (when add Correos Standard Delivery for Spain demo shops, status is enabled )
        shipping.sh es false false 6aac1a96-b71e-3b00-94ff-9fb21733bf5f (when add MRW for Spain all shops, status is disabled )
    when isDemo is false, the input shop list file name will be /input/all_shop_list.txt, which is set in merchant-tools.properties
    when isDemo is true, the input shop list file name will be /input/demo_shop_list.txt, which is set in merchant-tools.properties
    you can change the merchant api url settings in shipping.sh.
    after executing the program, you can read /logs/error.log to check whether any error happened or not.


(4) location.sh
    this program can make location csv files for certain marketplace . 
    the parameters are: mallId1 mallId2, for example , 
        location.sh my id (when make a csv file called gecp.csv for Malaysia and Indonesia )
    this program always make location csv file called gs.csv for global shopping marketplace locations.
    by default, locations of five marketplaces(my, id, es, th, sg) will be added into gecp.csv.
    you can change the merchant api url settings in location.sh.



(5) initial_shop_my.sh initial_shop_id.sh
    these programs can create shipping option and payment option for a certain shop . 
    the parameters are: shopId merchantId, the second parameter(merchantId) is optional. for example :
        initial_shop_my.sh 171888d0-c375-11e2-bb0f-005056bd02b6  (when add shipping and payment option for the Malaysia shop )
    you can change the merchant api url settings in initial_shop_my.sh initial_shop_id.sh.
    after executing the program, you can read /logs/error.log to check whether any error happened or not.


(6) shoponelist.sh
    this program can make a csv file that contains shop_id,merchant_id  of some specific shops. 
    the parameters are: shopId1, shopId2 ...., for example , 
        shoponelist.sh 00391690-9451-11e3-8913-005056a57ee8 b5af9e40-9465-11e3-8913-005056a57ee8 
                    or: mallId, shopUrl1, shopUrl2 ...., for example , 
        shoponelist.sh gs _ao_test0321-1-1848 aj_stg 
    you can change the merchant api url settings in the shoponelist.sh.
    after executing the program, you can read /logs/error.log to check whether any error happened or not.


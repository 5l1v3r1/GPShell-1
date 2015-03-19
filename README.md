# <span style="color:red"> Get status with APDU / Parameter (GPShell | GPTool)</span>
[1. Liens utiles](#ancre1)<br>
[2. Cartes utilisées](#ancre2)<br>
[3. Lecteur de carte](#ancre3)<br>
[4. Command](#ancre4)<br>
[5. Environnement](#ancre5)<br>
[6. Détail démo](#ancre6)<br>
[7. Code 9000](#ancre7)<br>
[8. Code 6A88](#ancre8)<br>
[9. Code 6A82](#ancre9)<br>
[10. Code 6A86](#ancre10)<br>
[11. Code 6985](#ancre11)<br>
[12. Code 6982](#ancre12)<br>
[13. cmd get_status](#ancre13)<br>
[13.1. List applets and packages and security domains P1:e0](#ancre14)<br>
[13.2. List packages P1:20](#ancre15)<br>
[13.3. List applets or security domains P1:40](#ancre16)<br>
[13.4. List Card Manager / Security Issuer Domain P1:80](#ancre17)<br>
[13.5. Get status state:1 Loaded](#ancre18)<br>
[13.6. Get status state:3 Installed](#ancre19)<br>
[13.7. Get status state:7 Activated](#ancre20)<br>
[14. GPTool](#ancre21)<br>
## <span style="color:blue"> 1. Liens utiles :</span><a id="ancre1"></a>
####-[sourceforge](http://sourceforge.net/p/globalplatform/wiki/GPShell/)<br>
####-[Oracle community](https://community.oracle.com/community/java/java_card/java_card_2)<br>
####-[github](https://github.com/pradeepk/globalplatform/tree/master/tags/GPShell-1.3.1/GPShell)<br>
####-[jveliot](http://jveliot.free.fr/blog/wp-content/smartcardpgonlinuxfornewbies.txt)<br>
## <span style="color:blue"> 2. Cartes utilisées :</span><a id="ancre2"></a>
**Les cartes utilisées sont des cartes en prp** <br>
**-Deux types de carte {orangePrp , NRJMobilePrp}**<br>

              1)carte SNCF ORANGE PRP0683139923
                  Gemalto
                  Msisdn : 1446262684974
                  NFC N9 49
              2)carte SNCF ORANGE PRP0686847435
                  Oberthur
                  Msisdn : 2446200550765
                  NFC N9 59
              3)carte CTS NRJ PRP0771167580
                  Gemalto
                  Msisdn : 3760133877586  
## <span style="color:blue">3. Lecteur de carte :</span><a id="ancre3"></a>
**OMNIKEY CardMan 5321v2**<br>
    _IC: 5345A-OK5321_<br>
    _FCC ID: SIYOK5321_<br>
[For driver see](https://www.hidglobal.com/)
### <span style="color:green"> Utilisation :</span>
**<span style="color:red">Remarque</span><br>**
Si le lecteur est utilisé en dehors de la chroot , il ne sera pas possible de l'utiliser à l'intérieur de cette dernière.<br>
Pour le démarrer il le faut le stopper en dehors de la chroot et faire un restart à l'intérieur.<br>
#### <span style="color:#00BFFF"> cmd :</span>
**Démarrer<br>`service pcscd start`<br>
**Arréter<br>`service pcscd stop`<br>
**Voir l'état<br>`service pcscd status`<br>
**Redémarrer<br>`service pcscd restart`<br>
**Scanner le lecteur<br>`pcsc_scan`<br>
## <span style="color:blue">4. Command :</span><a id="ancre4"></a>
        ```sh
        `mode_201` # Réglez le mode de protocole à OpenPlatform 2.0.1
        
        `mode_211` # Réglez le mode de protocole à GlobalPlatform 2.1.1
        
        `enable_trace` #A ctiver APDU trace,Vous verrez l'APDU envoyé en texte clair. Les deux derniers octets de la reponse sont le
        code de réponse. Un code de reponse de 9000 signifie le succès, sinon le code de réponse indique une erreur. Cela peut 
        être OK lors de la suppression d'une applet ou de l'emballage inexistante.
        
        `enable_timer` # Permet l'enregistrement du temps d'exécution d'une commande.
        
        `establish_context` # Établir le contexte
        
        `card_connect -reader ReaderName` # Connect à la carte dans le lecteur ReaderName
        
        `card_connect -readerNumber x` # Connectez-vous à carte dans le lecteur xième dans le système
        
        `open_sc -keyind x -keyver x touche xyz xyz -mac_key -enc_key xyz xyz -kek_key -sécurité x -scp x -scpimpl x -keyDerivation x`
        # Ouvrez canal sécurisé
        # Pour OpenPlatform 2.0.1 'carte ne -keyind -keyver -mac_key et enc_key sont nécessaires. Pour GlobalPlatform 2.1.1 cartes 
        -scp et -scpimpl devraient ne pas être nécessaire à l'approvisionnement. Vous devez également spécifier -kek_key. 
        Si votre carte supporte un protocole sécurisé canal mise en œuvre avec une seule clé de base, spécifiez cette clé avec
        touche et omettre les autres. Si vous avez une carte qui utilise la dérivation clé, vous devez activer le mode de 
        dérivation avec l'option -keyDerivation et vous devez spécifier avec touche la clé principale (de la mère).
        
        `release_context` #contexte de sortie
        
        `put_sc_key -keyver 0 -newkeyver 2 -mac_key new_MAC_key -enc_key new_ENC_key -kek_key new_KEK_key -cur_kek current_KEK_key` # Ajouter 
        nouveau jeu de clés la version 2
        
        `put_sc_key -keyver une -newkeyver une -mac_key new_MAC_key -enc_key new_ENC_key -kek_key new_KEK_key -cur_kek current_KEK_key`
        # Remplacer ensemble clé la version 1
        
        `put_dm_keys -keyver 0 -newkeyver 2 -file public_rsa_key_file -pass password -key new_receipt_generation_key` 
        # Mettez délégué pour GP 2.1.1 dans la version 2
        
        `send_apdu -sc 0 -APDU xxx xxx` # Envoyer APDU sans canal sécurisé,l'APDU est donné comme hex sans espaces et sans leadings 0x.
        
        `card_disconnect` # Carte Déconnecter
        
        `send_apdu_nostop -sc 0 -APDU xxx` # Ne s'arrête pas en cas d'erreur,l'APDU est donné comme hex sans espaces et sans leadings 0x.
        
        `get_data -identifier` # identifier qu'une commande GET DATA renvoi les données pour l'identifiant donné.
        
        `-mac_key , -enc_key , -kek_key` # valeur de la clé en hexadécial
        
        `-security x` # 0: clair, 1: MAC, 3: MAC + ENC
        
        ```
## <span style="color:blue">5. Environnement :</span><a id="ancre5"></a> 
`GLOBALPLATFORM_DEBUG`<br>
Active la sortie de débogage de la bibliothèque GlobalPlatform sous-jacent.<br>
`GLOBALPLATFORM_LOGFILE`<br>
Définit le nom du fichier journal pour la sortie de débogage.<br>
## <span style="color:blue">6. Détail démo :</span><a id="ancre6"></a>
        -lancement d'une cmd GPShell sur SetVariableGP.sh {./getStatusWithParameter.sh |sh getStatusWithParameter.sh}
           le script prendra un AID , un jeu de clé défini et un APDU dans le code par défaut { AID = A000000151000000
                                                                                      enc_key = 4953445F4F54415F4B5332305F4B4943
                                                                                      mac_key = 4953445F4F54415F4B5332305F4B4944
                                                                                      kek_key = 4953445F4F54415F4B5332305F4B494B
                                                                                      APDU = 80F28002024F00
                                                                                      }
           on peut aussi lui passer l'AID , les clés et l'APDU qu'on veut en paramètre dans la cmd :
                e.g : sh SetVariableGP.sh 0 1 2 3 4
                les variables sont inséré dans l'ordre dans la commande et dans le script (AID , enc_key , mac_key , kek_key , APDU)
## <span style="color:blue"> 7. Code 9000 : </span><a id="ancre7"></a>
### <span style="color:green">Script utilisé :
            #!/bin/bash
            
            aid=${1:-"A000000151000000"}
            enc_key=${2:-"4953445F4F54415F4B5332305F4B4943"}
            mac_key=${3:-"4953445F4F54415F4B5332305F4B4944"}
            kek_key=${4:-"4953445F4F54415F4B5332305F4B494B"}
            APDU=${5:-"80F28002024F00"}
            
            cat << EOF | gpshell
            mode_211
            enable_trace
            
            establish_context
            
            card_connect
            
            # OPEN SCP02 CHANNEL ON Orange ISD
            
            #SSD ISD
            select -AID ${aid}
            open_sc -security 1 -keyind 0 -keyver 32 -enc_key ${enc_key} -mac_key ${mac_key} -kek_key ${kek_key} -scp 2 
            -scpimpl 21
            
            # get status apdu
            #send_apdu -sc 1 -APDU 80F28002024F00
            send_apdu -sc 1 -APDU ${APDU}
            
            card_disconnect
            release_context
            EOF
### <span style="color:green">cmd :</span>
           $ sh getStatusWithParameter
### <span style="color:green">Trace d'exe :</span>
            mode_211
            enable_trace
            establish_context
            card_connect
            * reader name OMNIKEY CardMan (076B:5321) 5321 (OKCM0071710101035460093076760392) 00 00
            select -AID A000000151000000
            Command --> 00A4040008A000000151000000
            Wrapped command --> 00A4040008A000000151000000
            Response <-- 6F4D8408A000000151000000A541733B06072A864886FC6B01600B06092A864886FC6B020202630906072A864886FC6
            B03640B06092A864886FC6B040255640B06092A864886FC6B0480009F6501FF9000
            open_sc -security 1 -keyind 0 -keyver 32 -enc_key 4953445F4F54415F4B5332305F4B4943 -mac_key 4953445F4F54415F
            4B5332305F4B4944 -kek_key 4953445F4F54415F4B5332305F4B494B -scp 2 -scpimpl 21
            Command --> 8050200008062DD8D53DF6221400
            Wrapped command --> 8050200008062DD8D53DF6221400
            Response <-- 000000000000000000002002003C5EC94454B9315A557E87AA7D4D059000
            Command --> 84820100103ACF97BF22829F1AA969AE772214E780
            Wrapped command --> 84820100103ACF97BF22829F1AA969AE772214E780
            Response <-- 9000
            send_apdu -sc 1 -APDU 80F28002024F00
            Command --> 80F28002024F00
            Wrapped command --> 84F280020A4F00E241C013ABA1DAF9
            Response <-- E3164F08A0000001510000009F70020F01C5039AFE80EA009000
<span style="color: green">code AID :A000000151000000</span> <br>
<span style="color: green">code state : F7</span><br>
<span style="color: green">code succé : 9000</span><br>
<span style="color: green">code privilèges : 9A</span><b>

            send_APDU() returns 0x80209000 (9000: Success. No error.)
            card_disconnect
            release_context
## <span style="color:blue">8. Code 6A88 :</span><a id="ancre8"></a>
### <span style="color:green">Script utilisé :</span>
            mode_211
            enable_trace
            establish_context
            card_connect
            
            select -AID A000000151000000
            
            open_sc -security 1 -keyind 0 -keyver 32 -enc_key 4953445F4F54415F4B5332305F4B4943 -mac_key 4953445F4F54415F
            4B5332305F4B4944 -kek_key 4953445F4F54415F4B5332305F4B494B -scp 2 -scpimpl 21
            
            delete -AID D0D1D2D3D4D50101
            delete -AID D0D1D2D3D4D501
            
            install_for_load -pkgAID D0D1D2D3D4D501 -nvCodeLimit 1500  -sdAID A000000151000000
            
            load -file test.cap.transf
            
            install_for_install -instParam 00 -priv 02 -AID D0D1D2D3D4D50101 -pkgAID D0D1D2D3D4D501 -instAID D0D1D2D3D4D
            
            50101 -nvDataLimit 500
            
            card_disconnect
            release_context
### <span style="color:green">cmd :</span>
            $ gpshell Try.gpshell
### <span style="color:green">Trace d'exe :</span>
            mode_211
            enable_trace
            establish_context
            card_connect
            * reader name OMNIKEY CardMan (076B:5321) 5321 (OKCM0071710101035460093076760392) 00 00
            select -AID A000000151000000
            Command --> 00A4040008A000000151000000
            Wrapped command --> 00A4040008A000000151000000
            Response <-- 6F7A8408A000000151000000A56E735F06072A864886FC6B01600B06092A864886FC6B020202630906072A864886FC6
            B03640B06092A864886FC6B048000640B06092A864886FC6B0402556622060A2B060104012A036E000106145354333346314D20012F0
            1020111012F010201189F6E060008203001189F6501FF9000
            open_sc -security 1 -keyind 0 -keyver 32 -enc_key 4953445F4F54415F4B5332305F4B4943 -mac_key 4953445F4F54415F
            4B5332305F4B4944 -kek_key 4953445F4F54415F4B5332305F4B494B -scp 2 -scpimpl 21
            Command --> 8050200008B71FFFC0A2E6E67700
            Wrapped command --> 8050200008B71FFFC0A2E6E67700
            Response <-- 0000604DC0098C762C002002002AEA39DB594B1EB055E69178A645A29000
            Command --> 8482010010E01F1F1AA20A8F56565129DEA7D0A090
            Wrapped command --> 8482010010E01F1F1AA20A8F56565129DEA7D0A090
            Response <-- 9000
            delete -AID D0D1D2D3D4D50101
            Command --> 80E400800A4F08D0D1D2D3D4D5010100
            Wrapped command --> 84E40080124F08D0D1D2D3D4D5010136C64F4626903E4600
            Response <-- 6A88
            delete() returns 0x80206A88 (6A88: Referenced data not found.)
            delete -AID D0D1D2D3D4D501
            Command --> 80E40080094F07D0D1D2D3D4D50100
            Wrapped command --> 84E40080114F07D0D1D2D3D4D50126AFC274857B598E00
            Response <-- 6A88
            delete() returns 0x80206A88 (6A88: Referenced data not found.)
<span style="color: red">code 6A88 : données référencées introuvables</span> <br>

            install_for_load -pkgAID D0D1D2D3D4D501 -nvCodeLimit 1500  -sdAID A000000151000000
            Command --> 80E602001A07D0D1D2D3D4D50108A0000001510000000006EF04C60205E80000
            Wrapped command --> 84E602002207D0D1D2D3D4D50108A0000001510000000006EF04C60205E8005F3E3F04D3175EE600
            Response <-- 009000
            load -file test.cap.transf
            file name test.cap.transf
            load() returns 0x00000002 (No such file or directory)
<span style="color: red">code 0x00000002 : Aucun fichier ou répertoire de ce nom/span> <br>
## <span style="color:blue">9. Code 6A82 :</span><a id="ancre9"></a>
### <span style="color:green">Script utilisé :</span>
            #!/bin/bash

            aid=${1:-"A000000151000000"}
            enc_key=${2:-"4953445F4F54415F4B5332305F4B4943"}
            mac_key=${3:-"4953445F4F54415F4B5332305F4B4944"}
            kek_key=${4:-"4953445F4F54415F4B5332305F4B494B"}
            APDU=${5:-"80F28002024F00"}

            cat << EOF | gpshell
            mode_211
            enable_trace

            establish_context

            card_connect

            # OPEN SCP02 CHANNEL ON Orange ISD

            #SSD ISD
            select -AID ${aid}
            open_sc -security 1 -keyind 0 -keyver 32 -enc_key ${enc_key} -mac_key ${mac_key} -kek_key ${kek_key} -scp 2
            -scpimpl 21

            # get status apdu
            #send_apdu -sc 1 -APDU 80F28002024F00
            send_apdu -sc 1 -APDU ${APDU}

            card_disconnect
            release_context
            EOF
### <span style="color:green">cmd :</span>
            $ sh SetVariableGP.sh 123 4953445F4F54415F4B5332305F4B4943 4953445F4F54415F4B5332305F4B4944
                  4953445F4F54415F4B5332305F4B494 80F28002024F00
### <span style="color:green">Trace d'exe :</span>
            mode_211
            enable_trace
            establish_context
            card_connect
            * reader name OMNIKEY CardMan (076B:5321) 5321 (OKCM0071710101035460093076760392) 00 00
            select -AID 123
            Command --> 00A40400021203
            Wrapped command --> 00A40400021203
            Response <-- 6A82
            select_application() returns 0x80216A82 (6A82: The application to be selected could not be found.)
<span style="color: red">code 6A82 : L'application qui doit être sélectionnée n'existe pas </span> <br>
## <span style="color:blue">10. Code 6A86 :</span><a id="ancre10"></a>
### <span style="color:green">Script utilisé :</span>
            #!/bin/bash

            aid=${1:-"A000000151000000"}
            enc_key=${2:-"4953445F4F54415F4B5332305F4B4943"}
            mac_key=${3:-"4953445F4F54415F4B5332305F4B4944"}
            kek_key=${4:-"4953445F4F54415F4B5332305F4B494B"}
            APDU=${5:-"80F28002024F00"}

            cat << EOF | gpshell
            mode_211
            enable_trace

            establish_context

            card_connect

            # OPEN SCP02 CHANNEL ON Orange ISD

            #SSD ISD
            select -AID ${aid}
            open_sc -security 2 -keyind 0 -keyver 32 -enc_key ${enc_key} -mac_key ${mac_key} -kek_key ${kek_key} -scp 2
            -scpimpl 21

            # get status apdu
            #send_apdu -sc 1 -APDU 80F28002024F00
            send_apdu -sc 1 -APDU ${APDU}

            card_disconnect
            release_context
            EOF
### <span style="color:green">cmd :</span>
            $ sh getStatusWithParameter.sh
### <span style="color:green">Trace d'exe :</span>
            mode_211
            enable_trace
            establish_context
            card_connect
            * reader name OMNIKEY CardMan (076B:5321) 5321 (OKCM0071710101035460093076760392) 00 00
            select -AID A000000151000000
            Command --> 00A4040008A000000151000000
            Wrapped command --> 00A4040008A000000151000000
            Response <-- 6F7A8408A000000151000000A56E735F06072A864886FC6B01600B06092A864886FC6B020202630906072A864886FC6
            B03640B06092A864886FC6B048000640B06092A864886FC6B0402556622060A2B060104012A036E000106145354333346314D20012F0
            1020111012F010201189F6E060008203001189F6501FF9000
            open_sc -security 2 -keyind 0 -keyver 32 -enc_key 4953445F4F54415F4B5332305daF4B4943 -mac_key 4953445F4F5441
            5F4B5332305F4B4944 -kek_key 4953445F4F54415F4B5332305F4B494B -scp 2 -scpimpl 21
            Command --> 8050200008F49F355437D74DD300
            Wrapped command --> 8050200008F49F355437D74DD300
            Response <-- 0000604DC0098C762C002002002E84B8039C9105BF3EBE25B422DD869000
            Command --> 84820200105AB84BF016F4920665605B476434CC1A
            Wrapped command --> 84820200105AB84BF016F4920665605B476434CC1A
            Response <-- 6A86
            mutual_authentication() returns 0x80206A86 (6A86: Incorrect parameters (P1, P2).)
<span style="color: red">code 6A86 : Les paramètres sont incorrects (P1, P2)</span> <br>
## <span style="color:blue">11. Code 6985 :</span><a id="ancre11"></a>
### <span style="color:green">Script utilisé :</span>
            #!/bin/bash

            aid=${1:-"A000000151000000"}
            enc_key=${2:-"4953445F4F54415F4B5332305F4B4943"}
            mac_key=${3:-"4953445F4F54415F4B5332305F4B4944"}
            kek_key=${4:-"4953445F4F54415F4B5332305F4B494B"}
            APDU=${5:-"80F28002024F00"}

            cat << EOF | gpshell
            mode_211
            enable_trace

            establish_context

            card_connect

            # OPEN SCP02 CHANNEL ON Orange ISD

            #SSD ISD
            select -AID ${aid}
            open_sc -security 0 -keyind 0 -keyver 32 -enc_key ${enc_key} -mac_key ${mac_key} -kek_key ${kek_key} -scp 2
            -scpimpl 21

            # get status apdu
            #send_apdu -sc 1 -APDU 80F28002024F00
            send_apdu -sc 1 -APDU ${APDU}

            card_disconnect
            release_context
            EOF
### <span style="color:green">cmd :</span>
            $ sh getStatusWithParameter.sh
### <span style="color:green">Trace d'exe :</span>
            mode_211
            enable_trace
            establish_context
            card_connect
            * reader name OMNIKEY CardMan (076B:5321) 5321 (OKCM0071710101035460093076760392) 00 00
            select -AID A000000151000000
            Command --> 00A4040008A000000151000000
            Wrapped command --> 00A4040008A000000151000000
            Response <-- 6F7A8408A000000151000000A56E735F06072A864886FC6B01600B06092A864886FC6B020202630906072A864886FC6
            B03640B06092A864886FC6B048000640B06092A864886FC6B0402556622060A2B060104012A036E000106145354333346314D20012F0
            1020111012F010201189F6E060008203001189F6501FF9000
            open_sc -security 0 -keyind 0 -keyver 32 -enc_key 4953445F4F54415F4B5332305F4B4943 -mac_key 4953445F4F54415F
            4B5332305F4B4944 -kek_key 4953445F4F54415F4B5332305F4B494B -scp 2 -scpimpl 21
            Command --> 805020000830DC58732E15D1C000
            Wrapped command --> 805020000830DC58732E15D1C000
            Response <-- 0000604DC0098C762C002002002FA3FE41D59B6052AA33F6572AA1769000
            Command --> 848200001051841C642DEE29E750BE1CF1A06052D3
            Wrapped command --> 848200001051841C642DEE29E750BE1CF1A06052D3
            Response <-- 6985
            mutual_authentication() returns 0x80206985 (6985: Command not allowed - Conditions of use not satisfied.)
<span style="color: red">code 6985 : Commande non autorisée - Conditions d'utilisation non satisfaites</span> <br>
## <span style="color:blue">12. Code 6982 :</span><a id="ancre12"></a>
### <span style="color:green">Script utilisé :</span>
            #!/bin/bash

            aid=${1:-"A000000151000000"}
            enc_key=${2:-"4953445F4F54415F4B5332305F4B4943"}
            mac_key=${3:-"4953445F4F54415F4B5332305F4B4944"}
            kek_key=${4:-"4953445F4F54415F4B5332305F4B494B"}
            APDU=${5:-"80F28002024F00"}

            cat << EOF | gpshell
            mode_211
            enable_trace

            establish_context

            card_connect

            # OPEN SCP02 CHANNEL ON Orange ISD

            #SSD ISD
            select -AID ${aid}
            open_sc -security 0 -keyind 0 -keyver 32 -enc_key ${enc_key} -mac_key ${mac_key} -kek_key ${kek_key} -scp 2
            -scpimpl 21

            # get status apdu
            #send_apdu -sc 1 -APDU 80F28002024F00
            send_apdu -sc 0 -APDU ${APDU}

            card_disconnect
            release_context
            EOF
### <span style="color:green">cmd :</span>
            $ sh getStatusWithParameter.sh
### <span style="color:green">Trace d'exe :</span>
            mode_211
            enable_trace
            establish_context
            card_connect
            * reader name OMNIKEY CardMan (076B:5321) 5321 (OKCM0071710101035460093076760392) 00 00
            select -AID A000000151000000
            Command --> 00A4040008A000000151000000
            Wrapped command --> 00A4040008A000000151000000
            Response <-- 6F7A8408A000000151000000A56E735F06072A864886FC6B01600B06092A864886FC6B020202630906072A864886FC6
            B03640B06092A864886FC6B048000640B06092A864886FC6B0402556622060A2B060104012A036E000106145354333346314D20012F0
            1020111012F010201189F6E060008203001189F6501FF9000
            open_sc -security 1 -keyind 0 -keyver 32 -enc_key 4953445F4F54415F4B5332305F4B4943 -mac_key 4953445F4F54415F
            4B5332305F4B4944 -kek_key 4953445F4F54415F4B5332305F4B494B -scp 2 -scpimpl 21
            Command --> 805020000841BE8342134290D400
            Wrapped command --> 805020000841BE8342134290D400
            Response <-- 0000604DC0098C762C0020020036801D9A16638F6FBB514E3F4FFD3B9000
            Command --> 8482010010ECDE61A1D2A33402A1FB4669353A17E9
            Wrapped command --> 8482010010ECDE61A1D2A33402A1FB4669353A17E9
            Response <-- 9000
            send_apdu -sc 0 -APDU 80F28002024F00
            Command --> 80F28002024F00
            Wrapped command --> 80F28002024F00
            Response <-- 6982
            send_APDU() returns 0x80206982 (6982: Command not allowed - Security status not satisfied.)
<span style="color: red">code 6982 : Commande non autorisée - État de sécurité non satisfaisant.</span> <br>

            card_disconnect
            release_context
## <span style="color:blue"> 13. cmd get_status :</span><a id="ancre13"></a>
            get_status -element e0 // List applets and packages and security domains
            
            get_status -element 20 // List packages
            
            get_status -element 40 // List applets or security domains
            
            get_status -element 80 // List Card Manager / Security Issuer Domain
### <span style="color:green"> Le retour de cmd :</span>
1)return commencant par a00 , exemple -> {a0000000620001,a0000000620101,a0000000620102...}<br>
`Card Management applets( factory installed )`<br>
2)return , exemple ->{893301ffff2150ff33ffff89434449,00010203040506070b ...}<br>
`Identity package AID`<br>
3)return , exemple ->{00010203040506070b01,893301ffff2000ff33ffff8943444901 ...}<br>
`Identity applet AID`<br>
### <span style="color:blue">13.1List applets and packages and security domains P1:e0 :</span><a id="ancre14"></a>
### <span style="color:green">Script utilisé :</span>
            #!/usr/bin/env gpshell
            # -*- coding: UTF8 -*-
            
            mode_211
            enable_trace
            establish_context
            card_connect
            
            select -AID A000000151000000
            
            open_sc -security 1 -keyind 0 -keyver 32 -enc_key 4953445F4F54415F4B5332305F4B4943 -mac_key 4953445F4F54415F
            4B5332305F4B4944 -kek_key 4953445F4F54415F4B5332305F4B494B -scp 2 -scpimpl 21
            
            get_status -element e0
            
            card_disconnect
            release_context
### <span style="color:green">cmd :</span>
            $ gpshell getStatusWithGPShellCmd.gpshell
### <span style="color:green">Trace d'exe :</span>
            mode_211
            enable_trace
            establish_context
            card_connect
            * reader name OMNIKEY CardMan (076B:5321) 5321 (OKCM0071710101035460093076760392) 00 00
            select -AID A000000151000000
            Command --> 00A4040008A000000151000000
            Wrapped command --> 00A4040008A000000151000000
            Response <-- 6F4D8408A000000151000000A541733B06072A864886FC6B01600B06092A864886FC6B020202630906072A864886FC6
            B03640B06092A864886FC6B040255640B06092A864886FC6B0480009F6501FF9000
            open_sc -security 1 -keyind 0 -keyver 32 -enc_key 4953445F4F54415F4B5332305F4B4943 -mac_key 4953445F4F54415F
            4B5332305F4B4944 -kek_key 4953445F4F54415F4B5332305F4B494B -scp 2 -scpimpl 21
            Command --> 8050200008EC9DE3CE1020174900
            Wrapped command --> 8050200008EC9DE3CE1020174900
            Response <-- 000000000000000000002002004205C7B50AA9357DAA799EEA736A629000
            Command --> 84820100105B8FB157325BDA7E6CA1ED3A7F3130E0
            Wrapped command --> 84820100105B8FB157325BDA7E6CA1ED3A7F3130E0
            Response <-- 9000
            get_status -element e0
            Command --> 80F2E000024F0000
<span style="color:green"> 80F2E0'00'24F0000 , 00 : First cmd</span>  
        
            Wrapped command --> 84F2E0000A4F006A1FDC6C76C9C26E00
            Response <-- 07A0000000620001010007A0000000620002010007A0000000620003010007A0000000620101010008A000000062010
            101010007A0000000620102010007A0000000620201010010A0000000090003FFFFFFFF8910710001010010A0000000090003FFFFFFF
            F8910710002010010A0000000090005FFFFFFFF8911000000010010A0000000090005FFFFFFFF8912000000010010A0000000090005F
            FFFFFFF8913000000010010A0000000871005FFFFFFFF8913100000010010A0000000871005FFFFFFFF8913200000010010A00000000
            90005FFFFFFFF8911010000010010A0000000871005FFFFFFFF891410000001006310
<span style="color:green"> code 6310 : More data</span>

            Command --> 80F2E001024F0000
            Wrapped command --> 84F2E0010A4F002B13E0834F8736D100
            Response <-- 10A0000000090002FF32FF2089BFFFBA02010010A0000000090005FFFFFFFF8916010000010010A0000000090005FFF
            FFFFF8916020100010010A0000000090005FFFFFFFF8916020200010008A00000006202080301000DA00000007750726F7869434D720
            10006A00000015100010007A0000000030000010006A00000015102010007A000000003535101000BA00000015153504341534401000
            CA000000151535043415344000F8007A0000001515350010010893301FFFF2000FF33FF0189000122000F8010893301FFFF2000FF33F
            F0189000123000FA00CA000000151535056415344000FC110A000000077010000141000000000000201006310
            Command --> 80F2E001024F0000
<span style="color:green"> 80F2E0'01'24F0000 , 01 : second cmd</span>    
      
            Wrapped command --> 84F2E0010A4F00D357E6FD9380BD5000
            Response <-- 10A0000000770100001410000000000001010010A000000077010000140000FE0000010007000FA0000000090002FF4
            4FF12890000E2010010A0000000090002FF44FF12890000E200070010893301FFFF2000FF33FFFF894D455250010010893301FFFF200
            0FF33FFFF894D455241070010A0000000770100001420510000000004010010A000000077010000140000FE0000030007000F893301F
            FFF2000FF33FFFF89524943010010893301FFFF2000FF33FFFF8952494301070010A0000000770100001710000000000004010010A00
            0000077010000170000FE00000400070010A000000077010000021005000000004E01006310
<span style="color:green"> code 6310 : More data</span>

            Command --> 80F2E001024F0000
            Wrapped command --> 84F2E0010A4F000EF4A8536E89120100
            Response <-- 0E325041592E5359532E444446303107000F893301FFFF2150FF33FFFF89434449010010893301FFFF2000FF33FFFF8
            94344490107009000
<span style="color:green"> code 9000 : OK More data</span>

            
            List of elements (AID state privileges)
            a0000000620001	1	0
            a0000000620002	1	0
            a0000000620003	1	0
            a0000000620101	1	0
            a000000062010101	1	0
            a0000000620102	1	0
            a0000000620201	1	0
            a0000000090003ffffffff8910710001	1	0
            a0000000090003ffffffff8910710002	1	0
            a0000000090005ffffffff8911000000	1	0
            a0000000090005ffffffff8912000000	1	0
            a0000000090005ffffffff8913000000	1	0
            a0000000871005ffffffff8913100000	1	0
            a0000000871005ffffffff8913200000	1	0
            a0000000090005ffffffff8911010000	1	0
            a0000000871005ffffffff8914100000	1	0
            a0000000090002ff32ff2089bfffba02	1	0
            a0000000090005ffffffff8916010000	1	0
            a0000000090005ffffffff8916020100	1	0
            a0000000090005ffffffff8916020200	1	0
            a000000062020803	1	0
            a00000007750726f7869434d72	1	0
            a00000015100	1	0
            a0000000030000	1	0
            a00000015102	1	0
            a0000000035351	1	0
            a000000151535043415344	1	0
            a00000015153504341534400	f	80
            a0000001515350	1	0
            893301ffff2000ff33ff018900012200	f	80
            893301ffff2000ff33ff018900012300	f	a0
            a00000015153505641534400	f	c1
            a0000000770100001410000000000002	1	0
            a0000000770100001410000000000001	1	0
            a000000077010000140000fe00000100	7	0
            a0000000090002ff44ff12890000e2	1	0
            a0000000090002ff44ff12890000e200	7	0
            893301ffff2000ff33ffff894d455250	1	0
            893301ffff2000ff33ffff894d455241	7	0
            a0000000770100001420510000000004	1	0
            a000000077010000140000fe00000300	7	0
            893301ffff2000ff33ffff89524943	1	0
            893301ffff2000ff33ffff8952494301	7	0
            a0000000770100001710000000000004	1	0
            a000000077010000170000fe00000400	7	0
            a000000077010000021005000000004e	1	0
            325041592e5359532e4444463031	7	0
            893301ffff2150ff33ffff89434449	1	0
            893301ffff2000ff33ffff8943444901	7	0
            card_disconnect
            release_context
### <span style="color:blue">13.2 List packages P1:20 :</span><a id="ancre15"></a>
### <span style="color:green">Script utilisé :</span>
            #!/usr/bin/env gpshell
            # -*- coding: UTF8 -*-
            
            mode_211
            enable_trace
            establish_context
            card_connect
            
            select -AID A000000151000000
            
            open_sc -security 1 -keyind 0 -keyver 32 -enc_key 4953445F4F54415F4B5332305F4B4943 -mac_key 4953445F4F54415F
            4B5332305F4B4944 -kek_key 4953445F4F54415F4B5332305F4B494B -scp 2 -scpimpl 21
            
            get_status -element 20
            
            card_disconnect
            release_context
### <span style="color:green">cmd :</span>
            $ gpshell getStatusWithGPShellCmd.gpshell
###<span style="color:green">Trace d'exe :</span>
            mode_211
            enable_trace
            establish_context
            card_connect
            * reader name OMNIKEY CardMan (076B:5321) 5321 (OKCM0071710101035460093076760392) 00 00
            select -AID A000000151000000
            Command --> 00A4040008A000000151000000
            Wrapped command --> 00A4040008A000000151000000
            Response <-- 6F4D8408A000000151000000A541733B06072A864886FC6B01600B06092A864886FC6B020202630906072A864886FC6
            B03640B06092A864886FC6B040255640B06092A864886FC6B0480009F6501FF9000
            open_sc -security 1 -keyind 0 -keyver 32 -enc_key 4953445F4F54415F4B5332305F4B4943 -mac_key 4953445F4F54415F
            4B5332305F4B4944 -kek_key 4953445F4F54415F4B5332305F4B494B -scp 2 -scpimpl 21
            Command --> 80502000080865AD14FDB28B9100
            Wrapped command --> 80502000080865AD14FDB28B9100
            Response <-- 00000000000000000000200200412006081B5EAA4E2D7F4DB99A0ED09000
            Command --> 848201001010AE2128982CBA17EC3C56309AC26385
            Wrapped command --> 848201001010AE2128982CBA17EC3C56309AC26385
            Response <-- 9000
            get_status -element 20
            Command --> 80F22000024F0000
            Wrapped command --> 84F220000A4F004F0B9405A95E0B8B00
            Response <-- 07A0000000620001010007A0000000620002010007A0000000620003010007A0000000620101010008A000000062010
            101010007A0000000620102010007A0000000620201010010A0000000090003FFFFFFFF8910710001010010A0000000090003FFFFFFF
            F8910710002010010A0000000090005FFFFFFFF8911000000010010A0000000090005FFFFFFFF8912000000010010A0000000090005F
            FFFFFFF8913000000010010A0000000871005FFFFFFFF8913100000010010A0000000871005FFFFFFFF8913200000010010A00000000
            90005FFFFFFFF8911010000010010A0000000871005FFFFFFFF891410000001006310
            Command --> 80F22001024F0000
            Wrapped command --> 84F220010A4F004FFF942DCB60E45D00
            Response <-- 10A0000000090002FF32FF2089BFFFBA02010010A0000000090005FFFFFFFF8916010000010010A0000000090005FFF
            FFFFF8916020100010010A0000000090005FFFFFFFF8916020200010008A00000006202080301000DA00000007750726F7869434D720
            10006A00000015100010007A0000000030000010006A00000015102010007A000000003535101000BA00000015153504341534401000
            7A0000001515350010010A0000000770100001410000000000002010010A000000077010000141000000000000101000FA0000000090
            002FF44FF12890000E2010010893301FFFF2000FF33FFFF894D45525001006310
            Command --> 80F22001024F0000
            Wrapped command --> 84F220010A4F0024450F471D34E56B00
            Response <-- 10A000000077010000142051000000000401000F893301FFFF2000FF33FFFF89524943010010A000000077010000171
            0000000000004010010A000000077010000021005000000004E01000F893301FFFF2150FF33FFFF8943444901009000
            
            List of elements (AID state privileges)
            a0000000620001	1	0
            a0000000620002	1	0
            a0000000620003	1	0
            a0000000620101	1	0
            a000000062010101	1	0
            a0000000620102	1	0
            a0000000620201	1	0
            a0000000090003ffffffff8910710001	1	0
            a0000000090003ffffffff8910710002	1	0
            a0000000090005ffffffff8911000000	1	0
            a0000000090005ffffffff8912000000	1	0
            a0000000090005ffffffff8913000000	1	0
            a0000000871005ffffffff8913100000	1	0
            a0000000871005ffffffff8913200000	1	0
            a0000000090005ffffffff8911010000	1	0
            a0000000871005ffffffff8914100000	1	0
            a0000000090002ff32ff2089bfffba02	1	0
            a0000000090005ffffffff8916010000	1	0
            a0000000090005ffffffff8916020100	1	0
            a0000000090005ffffffff8916020200	1	0
            a000000062020803	1	0
            a00000007750726f7869434d72	1	0
            a00000015100	1	0
            a0000000030000	1	0
            a00000015102	1	0
            a0000000035351	1	0
            a000000151535043415344	1	0
            a0000001515350	1	0
            a0000000770100001410000000000002	1	0
            a0000000770100001410000000000001	1	0
            a0000000090002ff44ff12890000e2	1	0
            893301ffff2000ff33ffff894d455250	1	0
            a0000000770100001420510000000004	1	0
            893301ffff2000ff33ffff89524943	1	0
            a0000000770100001710000000000004	1	0
            a000000077010000021005000000004e	1	0
            893301ffff2150ff33ffff89434449	1	0
            card_disconnect
            release_context
### <span style="color:blue">13.3 List applets or security domains P1:40 :</span><a id="ancre16"></a>
### <span style="color:green">Script utilisé :</span>
            #!/usr/bin/env gpshell
            # -*- coding: UTF8 -*-
            
            mode_211
            enable_trace
            establish_context
            card_connect
            
            select -AID A000000151000000
            
            open_sc -security 1 -keyind 0 -keyver 32 -enc_key 4953445F4F54415F4B5332305F4B4943 -mac_key 4953445F4F54415F
            4B5332305F4B4944 -kek_key 4953445F4F54415F4B5332305F4B494B -scp 2 -scpimpl 21
            
            get_status -element 40
            
            card_disconnect
            release_context
### <span style="color:green">cmd :</span>
            $ gpshell getStatusWithGPShellCmd.gpshell
### <span style="color:green">Trace d'exe :</span>
            mode_211
            enable_trace
            establish_context
            card_connect
            * reader name OMNIKEY CardMan (076B:5321) 5321 (OKCM0071710101035460093076760392) 00 00
            select -AID A000000151000000
            Command --> 00A4040008A000000151000000
            Wrapped command --> 00A4040008A000000151000000
            Response <-- 6F4D8408A000000151000000A541733B06072A864886FC6B01600B06092A864886FC6B020202630906072A864886FC6
            B03640B06092A864886FC6B040255640B06092A864886FC6B0480009F6501FF9000
            open_sc -security 1 -keyind 0 -keyver 32 -enc_key 4953445F4F54415F4B5332305F4B4943 -mac_key 4953445F4F54415F
            4B5332305F4B4944 -kek_key 4953445F4F54415F4B5332305F4B494B -scp 2 -scpimpl 21
            Command --> 8050200008FD413DE5E4148E7A00
            Wrapped command --> 8050200008FD413DE5E4148E7A00
            Response <-- 0000000000000000000020020040F6DBDA0DA0CA23C26ED8DDC2F2A29000
            Command --> 84820100100660FA14A0C596F8D4F24CBE257B2078
            Wrapped command --> 84820100100660FA14A0C596F8D4F24CBE257B2078
            Response <-- 9000
            get_status -element 40
            Command --> 80F24000024F0000
            Wrapped command --> 84F240000A4F009A26388831973DF800
            Response <-- 0CA000000151535043415344000F8010893301FFFF2000FF33FF0189000122000F8010893301FFFF2000FF33FF01890
            00123000FA00CA000000151535056415344000FC110A000000077010000140000FE00000100070010A0000000090002FF44FF1289000
            0E200070010893301FFFF2000FF33FFFF894D455241070010A000000077010000140000FE00000300070010893301FFFF2000FF33FFF
            F8952494301070010A000000077010000170000FE0000040007000E325041592E5359532E4444463031070010893301FFFF2000FF33F
            FFF894344490107009000
            
            List of elements (AID state privileges)
            a00000015153504341534400	f	80
            893301ffff2000ff33ff018900012200	f	80
            893301ffff2000ff33ff018900012300	f	a0
            a00000015153505641534400	f	c1
            a000000077010000140000fe00000100	7	0
            a0000000090002ff44ff12890000e200	7	0
            893301ffff2000ff33ffff894d455241	7	0
            a000000077010000140000fe00000300	7	0
            893301ffff2000ff33ffff8952494301	7	0
            a000000077010000170000fe00000400	7	0
            325041592e5359532e4444463031	7	0
            893301ffff2000ff33ffff8943444901	7	0
            card_disconnect
            release_context
### <span style="color:blue">13.4 List Card Manager / Security Issuer Domain P1:80 :</span><a id="ancre17"></a>
### <span style="color:green">Script utilisé :</span>
            #!/usr/bin/env gpshell
            # -*- coding: UTF8 -*-
            
            mode_211
            enable_trace
            establish_context
            card_connect
            
            select -AID A000000151000000
            
            open_sc -security 1 -keyind 0 -keyver 32 -enc_key 4953445F4F54415F4B5332305F4B4943 -mac_key 4953445F4F54415F
            4B5332305F4B4944 -kek_key 4953445F4F54415F4B5332305F4B494B -scp 2 -scpimpl 21
            
            get_status -element 80
            
            card_disconnect
            release_context
### <span style="color:green">cmd :</span>
            $ gpshell getStatusWithGPShellCmd.gpshell
### <span style="color:green">Trace d'exe :</span>
            mode_211
            enable_trace
            establish_context
            card_connect
            * reader name OMNIKEY CardMan (076B:5321) 5321 (OKCM0071710101035460093076760392) 00 00
            select -AID A000000151000000
            Command --> 00A4040008A000000151000000
            Wrapped command --> 00A4040008A000000151000000
            Response <-- 6F4D8408A000000151000000A541733B06072A864886FC6B01600B06092A864886FC6B020202630906072A864886FC6
            B03640B06092A864886FC6B040255640B06092A864886FC6B0480009F6501FF9000
            open_sc -security 1 -keyind 0 -keyver 32 -enc_key 4953445F4F54415F4B5332305F4B4943 -mac_key 4953445F4F54415F
            4B5332305F4B4944 -kek_key 4953445F4F54415F4B5332305F4B494B -scp 2 -scpimpl 21
            Command --> 80502000084B32BF0A1DB88B7200
            Wrapped command --> 80502000084B32BF0A1DB88B7200
            Response <-- 000000000000000000002002003FC1D80EB1C61A4A0D260E65A0033A9000
            Command --> 848201001054EDD7F6A21189CA6D25EC6DE6009B82
            Wrapped command --> 848201001054EDD7F6A21189CA6D25EC6DE6009B82
            Response <-- 9000
            get_status -element 80
            Command --> 80F28000024F0000
            Wrapped command --> 84F280000A4F005FB61DAF456C791B00
            Response <-- 08A0000001510000000F9A9000
            
            List of elements (AID state privileges)
            a000000151000000	f	9a
            card_disconnect
            release_context
### <span style="color:blue"> Script get_status with param targeded :<span>
<span style="color:green"> **Script :**<br> <span>

            #!/bin/bash
            
            aid=${1:-"A000000151000000"}
            enc_key=${2:-"4953445F4F54415F4B5332305F4B4943"}
            mac_key=${3:-"4953445F4F54415F4B5332305F4B4944"}
            kek_key=${4:-"4953445F4F54415F4B5332305F4B494B"}
            apdu=${5:-"80F28002024F00"}
            element=${6:-"e0"}
            
            cat << EOF | gpshell
            mode_211
            enable_trace
            
            establish_context
            
            card_connect
            
            #SSD ISD
            select -AID ${aid}
            
            open_sc -security 1 -keyind 0 -keyver 32 -enc_key ${enc_key} -mac_key ${mac_key} -kek_key ${kek_key} -scp 2 -scpimpl 21
            
            # get status apdu
            send_apdu -sc 1 -APDU ${apdu}
            
            get_status -element ${element}
            
            card_disconnect
            release_context
            EOF
            
<span style="color:green"> **cmd :**<br> <span>

            $ sh getStatusTargededAID.sh {aid} {enc_key} {mac_key} {kek_key} {apdu} {element} 

### <span style="color:blue">13.5 Get status state:1 Loaded :</span><a id="ancre18"></a>
### <span style="color:green"> Référence GP doc :</span>
**Tableaux de state détaillé disponible à la section 11.1.1 dans le document [Global Platform Card Specification 2.2.0.7](http://www.win.tue.nl/pinpasjc/docs/GPCardSpec_v2.2.pdf)**
#### <span style="color:green">Script utilisé :</span>
**_getStatusWithGPShellCmd.gpshell_** (fns/tools/GPShell)

            #!/usr/bin/env gpshell
            # -*- coding: UTF8 -*-
            mode_211
            enable_trace
            establish_context
            card_connect
            
            select -AID A000000151000000
            
            open_sc -security 1 -keyind 0 -keyver 32 -enc_key 4953445F4F54415F4B5332305F4B4943 -mac_key 4953445F4F54415F
            4B5332305F4B4944 -kek_key 4953445F4F54415F4B5332305F4B494B -scp 2 -scpimpl 21
            
            #install -file helloWorld.cap -nvDataLimit 500 -instParam 00 -priv 2
            
            get_status -element 40
**On utilise comme paramétre P1 :40 pour lister les applets dans le security domain.<br>
On essaye d'utiliser un script qui nous renvoi une résultat fiable par rapport à nos clés de sécurités et la SIM qu'on 
utilise.**

            card_disconnect
            release_context
#### <span style="color:green">Trace d'exe de get_status :</span>
            mode_211
            enable_trace
            establish_context
            card_connect
            * reader name OMNIKEY CardMan (076B:5321) 5321 (OKCM0071710101035460093076760392) 00 00
            select -AID A000000151000000
            Command --> 00A4040008A000000151000000
            Wrapped command --> 00A4040008A000000151000000
            Response <-- 6F7A8408A000000151000000A56E735F06072A864886FC6B01600B06092A864886FC6B020202630906072A864886FC6
            B03640B06092A864886FC6B048000640B06092A864886FC6B0402556622060A2B060104012A036E000106145354333346314D20012F0
            1020111012F010201189F6E060008203001189F6501FF9000
            open_sc -security 1 -keyind 0 -keyver 32 -enc_key 4953445F4F54415F4B5332305F4B4943 -mac_key 4953445F4F54415F
            4B5332305F4B4944 -kek_key 4953445F4F54415F4B5332305F4B494B -scp 2 -scpimpl 21
            Command --> 8050200008C873B05CA35AA37300
            Wrapped command --> 8050200008C873B05CA35AA37300
            Response <-- 0000604DC0098C762C0020020069B3540A3A0F54E9DD46B51AEACA069000
            Command --> 84820100104BB4F8EE9594B33B637BF3240AEB1B8B
            Wrapped command --> 84820100104BB4F8EE9594B33B637BF3240AEB1B8B
            Response <-- 9000
            get_status -element 40 
            Command --> 80F24000024F0000
            Wrapped command --> 84F240000A4F00A8AE4DD9DD85421B00
            Response <-- 0FA000000018210C030000000000000207000FA00000001820020203000000425058070010A0000000871003FF33FFF
            F89B0000000070010A0000000871003FF33FFFF89B0000100070010A0000000090002FF33FFFF89B000100007000CA00000015153504
            3415344000F8010A0000000090002FF44FF12890000E200070010893301FFFF2000FF33FFFF894D455241070010893301FFFF2000FF3
            3FFFF8943444901070210893301FFFF2000FF33FFFF895249430107000CA0000000309007A30000011107000E325041592E5359532E4
            44446303107000EA0000000184D696661726543020107000EA0000000184D696661726543010107006310
            Command --> 80F24001024F0000
            Wrapped command --> 84F240010A4F000BCB72BF47DE92F900
            Response <-- 0CA000000151535056415344000FC10FA00000001820010581FF0000524A4507000CA000000063504B43532D3135070
            810A0000000871002FF33FF018900000100070810A0000000090001FF33FFFF89C0000000070810A0000000871001FF33FFFF8901010
            100070E0FA00000001810010801000000BAFE021FC09000
**la partie de l'output qui nous interesse :**<br>
    -les trois colonnes espacées correspendent respectivement à : AID / State / Privileges<br>
    <span style="color:red">-Voir => à droite de l'output</span>
    
            List of elements (AID state privileges)
            a000000018210c0300000000000002  7       0           => Card Management applets( factory installed )
            a00000001820020203000000425058  1       0           => Card Management applets( factory installed )
            a0000000871003ff33ffff89b0000000        7       0   => Identity package AID
            a0000000871003ff33ffff89b0000100        7       0   => Identity package AID
            a0000000090002ff33ffff89b0001000        7       0   => Identity package AID
            a00000015153504341534400        f       80          => Card Management applets( factory installed )
            a0000000090002ff44ff12890000e200        7       0   => Identity package AID
            893301ffff2000ff33ffff894d455241        7       0   => Identity package AID
            893301ffff2000ff33ffff8943444901        7       2   => Identity applet AID
            893301ffff2000ff33ffff8952494301        7       0   => Identity package AID
<span style="color:red"> **_Remarque:_**</span><br>
les states de nos AID sont à state=1<br>
**state 1 = Loaded**<br>

            a0000000309007a300000111        1       0       }
            325041592e5359532e4444463031    1       0       }      
            325041592e5359532e4444463031    1       0       }
            a0000000184d6966617265430201    1       0       }
            a0000000184d6966617265430101    1       0       }
            a00000015153505641534400        f       c1      }
            a00000001820010581ff0000524a45  1       0       }   => Card Management applets( factory installed )
            a000000063504b43532d3135        1       8       }
            a0000000871002ff33ff018900000100        7       }
            a0000000090001ff33ffff89c0000000        7       }
            a0000000871001ff33ffff8901010100        7       }
            a00000001810010801000000bafe02  1f      c0      }
            card_disconnect
            release_context
### <span style="color:blue">13.7 Get status state:3 Installed :</span><a id="ancre19"></a>
#### <span style="color:green">Script utilisé :</span>
**_getStatusWithGPShellCmd.gpshell_** <br>
 le même script qu'on a utilisé dans section d'avant <br>
 On va aussi activer l'instance installé sur la carte avec ce scripte et voir s'il y a un changement dans l'output :<br>
 **_5-sim_installInstances.gpshell_** (fns/tools/GPShell)
 
            #!/usr/bin/env gpshell
            
            mode_211
            enable_trace
            
            establish_context
            
            card_connect
            
            # SSD ISD
            select -AID A000000151000000
            open_sc -security 1 -keyind 0 -keyver 32 -enc_key 4953445F4F54415F4B5332305F4B4943 -mac_key 4953445F4F54415F
            4B5332305F4B4944 -kek_key 4953445F4F54415F4B5332305F4B494B -scp 2 -scpimpl 21
            
            # activate T2
            send_apdu -sc 1 -APDU 80E608001200000BA000000291A0000001910201000000
            
            card_disconnect
            release_context
#### <span style="color:green">Trace d'exe de get_status :</span>
            a000000018210c0300000000000002	7	0
            a00000001820020203000000425058	7	0
            a0000000871003ff33ffff89b0000000	7	0
            a0000000871003ff33ffff89b0000100	7	0
            a0000000090002ff33ffff89b0001000	7	0
            a00000015153504341534400		f	80
            a0000000090002ff44ff12890000e200	7	0
            893301ffff2000ff33ffff894d455241	7	0
            893301ffff2000ff33ffff8943444901	7	2
            893301ffff2000ff33ffff8952494301	7	0
            a0000004040325099901	3	0
            a000000291a00000019102	3	0
            f8524154502e43414c2e5374612d3032	3	0
<span style="color:red"> **_Remarque:_**</span><br>
ajout de trois nouveaux AID qui ont un state=3<br>
**state 3 = Installed**<br>

            a0000000309007a300000111	7	0
            325041592e5359532e4444463031	7	0
            a0000000184d6966617265430201	7	0
            a0000000184d6966617265430101	7	0
            a00000015153505641534400		f	c1
            a00000001820010581ff0000524a45	7	0
            a000000063504b43532d3135		7	8
            a0000000871002ff33ff018900000100	7	8
            a0000000090001ff33ffff89c0000000	7	8
            a0000000871001ff33ffff8901010100	7	e
            a00000001810010801000000bafe02	1f	c0
### <span style="color:blue">13.6 Get status state:7 Activated :</span><a id="ancre20"></a>
#### <span style="color:green">Script utilisé :</span>
**_getStatusWithGPShellCmd.gpshell_** <br>
 le même script qu'on a utilisé dans section d'avant <br>
 On va aussi installer une instance sur la carte avec ce script et voir s'il y a un changement dans l'output:<br>
 **_2-sim_activateInstances.gpshell_** (fns/tools/GPShell)
 
            #!/usr/bin/env gpshell
            
            mode_211
            enable_trace
            
            establish_context
            
            card_connect
            
            # SSD ISD
            select -AID A000000151000000
            open_sc -security 1 -keyind 0 -keyver 32 -enc_key 4953445F4F54415F4B5332305F4B4943 -mac_key 4953445F4F54415F
            4B5332305F4B4944 -kek_key 4953445F4F54415F4B5332305F4B494B -scp 2 -scpimpl 21
            
            # install calypso setup module
            send_apdu -sc 1 -APDU 80E60400410DF8524154502E43414C5950534F10F8524154502E43414C2E53746174696310F8524154502E
            43414C2E5374612D303201000DC90B010201000201071102020000
            
            # calypso setup aid : F8 52 41 54 50 2E 43 41 4C 2E 53 74 61 74 69 63
            
            # install T2
            send_apdu -sc 1 -APDU 80E60400400DF8524154502E43414C5950534F0EF8524154502E43414C2E526576330BA000000291A00000
            019102010013C900EF0FA008810101A5038201C0A10388010100
            
            card_disconnect
            release_context
#### <span style="color:green">Trace d'exe de get_status :</span>
**la partie de l'output qui nous interesse :**<br>

            a000000018210c0300000000000002	7	0
            a00000001820020203000000425058	7	0
            a0000000871003ff33ffff89b0000000	7	0
            a0000000871003ff33ffff89b0000100	7	0
            a0000000090002ff33ffff89b0001000	7	0
            a00000015153504341534400		f	80
            a0000000090002ff44ff12890000e200	7	0
            893301ffff2000ff33ffff894d455241	7	0
            893301ffff2000ff33ffff8943444901	7	2
            893301ffff2000ff33ffff8952494301	7	0
            a0000004040325099901	7	0
            a000000291a00000019102	7	0
            f8524154502e43414c2e5374612d3032	3	0
            a0000000309007a300000111	7	0
<span style="color:red"> **_Remarque:_**</span><br>
les states de nos AID sont à state=7<br>
**state 7 = Activated**<br>

            325041592e5359532e4444463031	7	0
            a0000000184d6966617265430201	7	0
            a0000000184d6966617265430101	7	0
            a00000015153505641534400		f	c1
            a00000001820010581ff0000524a45	7	0
            a000000063504b43532d3135		7	8
            a0000000871002ff33ff018900000100	7	8
            a0000000090001ff33ffff89c0000000	7	8
            a0000000871001ff33ffff8901010100	7	e
            a00000001810010801000000bafe02	1f	c0
## <span style="color:blue">14. GPTool :</span><a id="ancre21"></a>
### <span style="color:green">Fichier workingcopy.${iccid} :</span>
C’est le fichier profil de travail de la carte, copié depuis les modèles contenus dans CardProfiles.<br>
Il doit donc, pareillement, respecter les contraintes de card.xsd. L’extension .${iccid} sert à indexer le fichier avec 
la carte insérée, après lecture de son ICCID.<br>
Ce fichier permet principalement de configurer les propriétés des Security Domain de la carte.<br><br>
<span style="color:red"> **Détail technique :**</span>

            <label> : code la valeur affichée dans l’onglet profile de GPTool
            
            <aid> : une valeur modifiable, qui donne l’AID du SD
            
            <ssd isISD="true"> : Chaque Security domain est bordé d’une balise <ssd>, l’ISD doit avoir son attribut isIsd à "true"
            
            <tar> : donne le tar ciblé en mode SCP80
            
            <spi> : donne les parameters de sécurité applicable selon les règles de la TS 102 225
            
            
            Scp02 = option i permise 15 ou 55 (attribut optionel)
            
            Scp80 = true ou false
            
            <keysets> : donne les données des different keysets du SD
                        
            <keyset> : détaille les valeurs d’un seul keyset, on trouve par exemple les attributs suivants :
                        
                     Id = keyset version ou keyset id
                     
                     Algo = Type d’algo utilisé :
                         -DES (implicitly known)
                         -DES-ECB
                         -DES-CBC
                         -RSA 
                            
**Il est possible d’ajouter des keyset qui ne gèrent pas uniquement le secure channel.
GPTool est capable de reconnaitre et d’utiliser les Keysets spécifiques suivants :**

            ‘70’ = Token
            ‘71’ = Receipt
            ‘73’ = VASD keyset (Mandated DAP)
            ‘74’ = CASD keyset
            ‘75’ = ciphered load file keyset
### <span style="color:green">Exemple de fichier workingcopy.${iccid} :</span>
**_workingCopy.8933018357200157560_** (fns/tools/GPShell/gptool)

            <?xml version="1.0" encoding="UTF-8"?>
            <card xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="card.xsd">
              <iccid>89330183572000157560</iccid>
              <msisdn>0607704932</msisdn>
              <securityDomains>
                <ssd isISD="true">
                  <label>ISD</label>
                  <aid>A000000151000000</aid>
                  <tar>000000</tar>
                  <spi>1221</spi>
                  <keysets>
                    <keyset id="4" algo="DES-ECB" scp02="15" scp80="true" counter="0A">
                      <key value="4953445F4F54415F4B5330345F4B4943" />
                      <key value="4953445F4F54415F4B5330345F4B4944" />
                      <key value="4953445F4F54415F4B5330345F4B494B" />
                    </keyset>
                    <keyset id="20" algo="DES-ECB" scp02="15" scp80="false" counter="01">
                      <key value="4953445F4F54415F4B5332305F4B4943" />
                      <key value="4953445F4F54415F4B5332305F4B4944" />
                      <key value="4953445F4F54415F4B5332305F4B494B" />
                    </keyset>
                  </keysets>
                </ssd>
                <ssd isISD="false">
                  <label>VASD</label>
                  <aid>A00000015153505641534400</aid>
                  <tar>C00000</tar>
                  <spi>1221</spi>
                  <keysets>
                    <keyset id="1" algo="3DES" file="" scp02="15" scp80="true" counter="0D">
                      <key value="11111111111111111111111111111111" />
                      <key value="11111111111111111111111111111111" />
                      <key value="11111111111111111111111111111111" />
                    </keyset>
                    <keyset id="73" algo="RSA" file="" scp80="true">
                      <n value="B84ABB20E96810F74A90749735D4640C6305A7BD05D6ADA1D0F38D9E2EA86869B60657968C02FF5D0E8F73F35A60DE11905B
                      3DE5EA130D5141F8F81C519DC4E04C3019690CA708BC3AC484430069EBEDA592C2F60FBDE7952B7A8CC73659AEB2F03E497F4FF942FB13
                      1170A43D40ED9DF76A5CA6839DE19B02C11ACCA6360EA3" />
                      <d value="9DAAE9DFE570B01B3182D70CCAF3F3C8992B2C6FCE3FF57ADC6E92798387771501B2F95A865CC626D21F2EDF43325EAAFE2A
                      E191032ADB98EA6D5BAFB6AFD8CF6D16036C181A10D9BF02DDE090ADD49C47B968F51E957AF7497D8D8233D7EC25E85F02513E1305352A
                      45545D157D81D0DF20ADCB03666943C1323FA47D629839" />
                      <e value="010001" />
                    </keyset>
                  </keysets>
                </ssd>
              </securityDomains>
            </card>




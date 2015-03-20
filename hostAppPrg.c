#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <smartcard.h>

int main()
{
     int ret, i, smartcard;
     unsigned char applet_id[100], apdu_len, apdu[300],response[300]={0};
     smartcard_info *context;

     smartcard=CONTACTLESS;
     //smartcard=CONTACT_BOT;
     
     if((context = smartcard_init(smartcard)) == NULL){
          printf("Smartcard Initi. Failed\n");
          return 0xBB;
     }
     
     

     /* checking for smartcard */
     while (1){
          if(ret=smartcard_is_present(context, smartcard)!=0)
               sleep(1);
          else
               break;
     }

     printf("Selecting Applet \n");
     i = 0;
     applet_id[i++]=0x00; applet_id[i++]=0xA4; applet_id[i++]=0x04; applet_id[i++]=0x00; applet_id[i++]=0x06;
     applet_id[i++]=0xA9; applet_id[i++]=0xBF; applet_id[i++]=0xA2; applet_id[i++]=0xB6; applet_id[i++]=0xB1; applet_id[i++]=0x3E; applet_id[i++]=0x7F;

     if ((ret=smartcard_select_applet(context, smartcard, applet_id, i))!=0){
          printf("select applet failed %02x\n",ret);
          return ;
     }
     printf("Applet Selection Success \n");

#if 1
     i=0;
     apdu[i++]=0xCF; apdu[i++]=0x10; apdu[i++]=0x04; apdu[i++]=0x04; apdu[i++]=0x7F;
     //apdu[i++]=0x3F; apdu[i++]=0x00;
     apdu_len = i;
     
     printf("Sending APDU command\n");
     if ((ret=smartcard_apdu(context, smartcard, apdu, apdu_len, response))!=0){
          printf("apdu failed %d\n",ret);
          //return;
     }else
          printf("APDU Success\n");
     
     //printf("\n");
#endif

     smartcard_deinit(context);
     return 0;     
}

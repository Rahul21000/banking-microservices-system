package com.example.loanApp.template;

public class EmailTemplate {
   static String emailTemplate =
           "<!DOCTYPE html>" +
                   "<html lang=\"en\">" +
                   "  <head>" +
                   "    <meta charset=\"UTF-8\" />" +
                   "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />" +
                   "    <link rel=\"preconnect\" href=\"https://fonts.googleapis.com\" />" +
                   "    <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin />" +
                   "    <link" +
                   "      href=\"https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,300;0,400;0,500;0,600;0,700;1,300&display=swap\"" +
                   "      rel=\"stylesheet\"" +
                   "    />" +
                   "    <title>Join Our New Community and Win Exciting Prizes!</title>" +
                   "    <style>" +
                   "      body {" +
                   "        margin: 0;" +
                   "      }" +
                   "      .table-width {" +
                   "        width: 55%;" +
                   "      }" +
                   "      @media only screen and (max-width: 767px) {" +
                   "        .table-width {" +
                   "          width: 100%;" +
                   "        }" +
                   "        .header-top {" +
                   "          padding-left: 15px;" +
                   "          padding-right: 15px;" +
                   "        }" +
                   "      }" +
                   "    </style>" +
                   "  </head>" +
                   "  <body>" +
                   "    <table" +
                   "      width=\"100%\"" +
                   "      style=\"" +
                   "        background-image: url('https://www.katikaa.com/assets/images/mail/bg-banner.png');" +
                   "        background-size: cover;" +
                   "        height: 100%;" +
                   "        padding-top: 30px;" +
                   "        padding-bottom: 30px;" +
                   "        font-family: Poppins, sans-serif;" +
                   "        border-collapse: separate !important;" +
                   "        border-spacing: 0;" +
                   "      \"" +
                   "    >" +
                   "      <tbody>" +
                   "        <tr>" +
                   "          <td>" +
                   "            <table" +
                   "              style=\"" +
                   "                margin: 0 auto;" +
                   "                background-color: #fff;" +
                   "                border-radius: 5px;" +
                   "                font-family: Poppins, sans-serif;" +
                   "                max-width: 800px;" +
                   "                padding: 15px;" +
                   "                width: 100%;" +
                   "              \"" +
                   "            >" +
                   "              <tbody>" +
                   "                <tr>" +
                   "                  <td colspan=\"2\" style=\"background: #0d002e; padding: 15px 20px\">" +
                   "                    <img" +
                   "                      src=\"https://www.katikaa.com/assets/images/mail/katikaa-logo.png\"" +
                   "                      alt=\"Katikaa Logo\"" +
                   "                      style=\"vertical-align: middle; filter: brightness(1); width: 130px; height: 47px;\"" +
                   "                    />" +
                   "                  </td>" +
                   "                </tr>" +
                   "                <tr>" +
                   "                  <td>" +
                   "                    <table style=\"padding: 12px 12px\" width=\"100%\">" +
                   "                      <tbody>" +
                   "                        <tr>" +
                   "                          <td colspan=\"2\" style=\"text-align: left\">" +
                   "                            <b style=\"color: #0d002e\">Hi {{playerName}},</b>" +
                   "                          </td>" +
                   "                        </tr>" +
                   "                        <tr>" +
                   "                          <td colspan=\"2\">" +
                   "                            <p" +
                   "                              style=\"" +
                   "                                margin: 0;" +
                   "                                padding-bottom: 10px;padding-top: 5px;" +
                   "                              \"" +
                   "                            >" +
                   "                             <b>We hope you're doing great!</b><br />" +
                   "                              On behalf of the, We are excited to inform you that" +
                   "                              you have emerged as the winner of our latest competition!" +
                   "                              Your outstanding performance has earned you the winning prize of" +
                   "                             . Congratulations! <br />" +
                   "                              Thank you for being a part of the ." +
                   "                              We look forward to your continued success in future events!<br />" +
                   "                            </p>" +
                   "                          </td>" +
                   "                        </tr>" +
                   "                         <tr>" +
                   "                          <td colspan=\"2\" style=\"text-align: left\">" +
                   "                            <p style=\"" +
                   "                                margin: 0;" +
                   "                                border-bottom: 1px solid #0d002e;" +
                   "                                padding-bottom: 30px;" +
                   "                                text-align: left" +
                   "                              \">" +
                   "                            <b style=\"color: #0d002e;\">community Name : {{communityName}}</b><br/>" +
                   "                            <b style=\"color: #0d002e\">winning prize : {{winningAmount}}</b>" +
                   "                            </p>" +
                   "                          </td>" +
                   "                        </tr>" +
                   "                        <tr>" +
                   "                          <td" +
                   "                            colspan=\"2\"" +
                   "                            style=\"text-align: left; padding-top: 10px\"" +
                   "                          >" +
                   "                            <b style=\"color: #0d002e\">Best regards,</b>" +
                   "                            <p style=\"margin: 0; padding-top: 5px\">" +
                   "                              Katikaa Team" +
                   "                            </p>" +
                   "                          </td>" +
                   "                        </tr>" +
                   "                      </tbody>" +
                   "                    </table>" +
                   "                  </td>" +
                   "                </tr>" +
                   "              </tbody>" +
                   "            </table>" +
                   "            <table" +
                   "              style=\"" +
                   "                margin: 0 auto;" +
                   "                background-color: #fff;" +
                   "                padding: 32px 12px;" +
                   "                margin-top: 25px;" +
                   "                border-radius: 5px;" +
                   "                font-family: Poppins, sans-serif;" +
                   "                border-collapse: separate !important;" +
                   "                border-spacing: 0;" +
                   "                width: 100%;" +
                   "                max-width: 800px;" +
                   "              \"" +
                   "            >" +
                   "              <tfoot>" +
                   "                <tr>" +
                   "                  <td" +
                   "                    style=\"" +
                   "                      font-size: 16px;" +
                   "                      font-weight: 500;" +
                   "                      color: #262626;" +
                   "                      margin: 0;" +
                   "                      text-align: center;" +
                   "                    \"" +
                   "                    colspan=\"2\"" +
                   "                  >" +
                   "                    Copyrights © 2024 All Rights Reserved by Katikaa." +
                   "                  </td>" +
                   "                </tr>" +
                   "              </tfoot>" +
                   "            </table>" +
                   "          </td>" +
                   "        </tr>" +
                   "      </tbody>" +
                   "    </table>" +
                   "  </body>" +
                   "</html>";
}

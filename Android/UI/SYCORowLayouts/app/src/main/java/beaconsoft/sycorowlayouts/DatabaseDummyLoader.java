package beaconsoft.sycorowlayouts;

/**
 * Created by Patrick on 2/23/2016.
 */
public class DatabaseDummyLoader {

    public static String[] boom = {
            /* CLEAR TABLES  */
            "DELETE FROM event          WHERE      event_id > 0;"
            , "DELETE FROM place          WHERE      place_id > 0;"
            , "DELETE FROM player         WHERE     player_id > 0;"
            , "DELETE FROM team           WHERE       team_id > 0;"
            , "DELETE FROM enrollment     WHERE enrollment_id > 0;"
            , "DELETE FROM users          WHERE       user_id > 0;"
            , "DELETE FROM league         WHERE     league_id > 0;"
            , "DELETE FROM sport          WHERE      sport_id > 0;"


            /* MAKE ADMIN AND 4 COACHES */
            , "INSERT INTO users VALUES(NULL, 'HENRY', 'JOHNSON', 5434327890,  2323548302, 'A.A@YAHOO.COM', 'ADMIN', 'PASS');"
            , "INSERT INTO sport (sport_id, sport_name) VALUES (NULL,     'SOCCER');"
            , "INSERT INTO sport (sport_id, sport_name) VALUES (NULL,   'FOOTBALL');"
            , "INSERT INTO sport (sport_id, sport_name) VALUES (NULL, 'BASKETBALL');"
            , "INSERT INTO sport (sport_id, sport_name) VALUES (NULL,       'POLO');"
            , "INSERT INTO sport (sport_id, sport_name) VALUES (NULL,     'TENNIS');"

            /* LEAGUE_ID AUTOINCREMENTED, USER_ID(ADMIN), LEAGUE_NAME, SPORT_ID, MIN_AGE, MAX_AGE, START_DATE, END_DATE */
            , "INSERT INTO league VALUES (NULL, 1, 'OXFORD PARKS AND RECREATION'       , 1,  4,  6, '15-MAR-2016', '15-JUN-2016');"
            , "INSERT INTO league VALUES (NULL, 1, 'DURHAM KICKERS'                    , 1,  7,  9, '15-MAR-2016', '15-JUN-2016');"
            , "INSERT INTO league VALUES (NULL, 1, 'GELA PARK SERVICE'                 , 2, 10, 12, '15-MAR-2016', '15-JUN-2016');"
            , "INSERT INTO league VALUES (NULL, 1, 'STOVALL-SHAW COMPETITIVE SPITTERS' , 3, 13, 15, '15-MAR-2016', '15-JUN-2016');"
            , "INSERT INTO league VALUES (NULL, 1, 'SAINT MARY''S SCHOOL FOR THE BLIND', 4,  5, 65, '18-MAR-2016', '18-JUN-2016');"
            , "INSERT INTO league VALUES (NULL, 1, 'MADISON CHEESE LEAGUE'             , 4, 18, 65, '20-APR-2016', '20-JUL-2016');"
            , "INSERT INTO users VALUES(NULL, 'COUNT', 'BASSY'   , 2548752145, 5326547852, 'BANGSOUNDTHORUGHD@WINDA.NET', 'COACH', 'PASS');"
            , "INSERT INTO users VALUES(NULL, 'SAM'  , 'NEIL'    , 9191594896, 1548693584, 'JURASSIC@PARK.AAH'          , 'COACH', 'PASS');"
            , "INSERT INTO users VALUES(NULL, 'PAT'  , 'COX'     , 9196934563, 7645378909, 'PAT.COX@MINEFIELDBIRD.NET'  , 'COACH', 'PASS');"
            , "INSERT INTO users VALUES(NULL, 'MIKE' , 'KERTCHER', 9587463784, 9588867754, 'KERTCHDADDY@SKINSONTOP.COM' , 'COACH', 'PASS');"

            //TEAM_ID AUTOINCREMENTED, LEAGUE_ID, TEAM_NAME, USER_ID(COACH USER_ID)
            , "INSERT INTO team VALUES(NULL, 1, 'YETIS', 1);"
            , "INSERT INTO team VALUES(NULL, 1, 'ROCKERS', 2);"
            , "INSERT INTO team VALUES(NULL, 1, 'SHELLERS', 3);"
            , "INSERT INTO team VALUES(NULL, 2, 'TAMS', 4);"
            , "INSERT INTO team VALUES(NULL, 2, 'YETIKILLERS', 1);"
            , "INSERT INTO team VALUES(NULL, 2, 'LIONS', 2);"
            , "INSERT INTO team VALUES(NULL, 3, 'REDSKINS', 3);"
            , "INSERT INTO team VALUES(NULL, 3, 'GIANTS', 4);"
            , "INSERT INTO team VALUES(NULL, 4, 'REDSOX', 1);"
            , "INSERT INTO team VALUES(NULL, 4, 'CHARGERS', 2);"
            , "INSERT INTO team VALUES(NULL, 5, 'LAKERS', 3);"
            , "INSERT INTO team VALUES(NULL, 5, 'KNICKS', 4);"
            , "INSERT INTO team VALUES(NULL, 6, 'PICKEDLASTS', 5);"

            //USER_ID AUTOINCREMENTED, FIRST, LAST, PHONE, EMERGENCY, EMAIL, USER_TYPE, PASS
            , "INSERT INTO users VALUES(NULL, 'PATRICIA',  'ARQUETTE',       9195772938, 8488372987, 'PATARQ@GODADDY.COM',                'USER','PASS');" //6
            , "INSERT INTO users VALUES(NULL, 'TIMOTHY',   'SWEET',          5684596325, 5467913468, 'HOTDUDE@SMELLSLIKEBACON.NET',       'USER','PASS');" //7
            , "INSERT INTO users VALUES(NULL, 'SNARKY',    'PARENT',         5847856983, 1485214852, 'IGAVEBIRTHTOSATAN@HITLERYOUTH.EDU', 'USER','PASS');" //8
            , "INSERT INTO users VALUES(NULL, 'TOM',       'CLANCY',         1548965325, 5848569587, 'TOOMANYCRIMENOVELS@MYDESK.COM',     'USER','PASS');" //9
            , "INSERT INTO users VALUES(NULL, 'KERRI',     'PLYMOUTH',       5124845796, 1547854256, 'LANDEDONMAY@THEROCK.COM',           'USER','PASS');" //10
            , "INSERT INTO users VALUES(NULL, 'BARRY',     'LONGFELLOW',     1548512457, 2563526589, 'STRETCH@ARMSTRONG.COM',             'USER','PASS');" //11
            , "INSERT INTO users VALUES(NULL, 'INPUT',     'M''DHATA',       1010010100, 0100100001, 'ONOFFOFFON@SOCKET.COM',             'USER','PASS');" //12
            , "INSERT INTO users VALUES(NULL, 'MESO',      'SAHOOP',         3451849765, 3164527548, 'HEALTHYLIVING@FAREAST.NET',         'USER','PASS');" //13
            , "INSERT INTO users VALUES(NULL, 'GUY',       'CLARK',          5431642758, 1245679185, 'LIVE@THEBLUEBIRDCAFE.COM',          'USER','PASS');" //14
            , "INSERT INTO users VALUES(NULL, 'CHERRIE',   'BERRY',          6645122436, 5487548151, 'LOVEINANELEVAT@AEROLIFT.COM',       'USER','PASS');" //15
            , "INSERT INTO users VALUES(NULL, 'COUNT',     'CHOCULA',        3164527548, 6664516666, 'ILOVETOCOUNTTHINGS@ANYWHERE.COM',   'USER','PASS');" //16
            , "INSERT INTO users VALUES(NULL, 'PATRICIA',  'ARQUETTE',       9195772938, 8488372987, 'PATARQ@GODADDY.COM',                'USER','PASS');" //17
            , "INSERT INTO users VALUES(NULL, 'TIMOTHY',   'SWEET',          5684596325, 5467913468, 'HOTDUD@SMELLS.NET',                 'USER','PASS');" //18
            , "INSERT INTO users VALUES(NULL, 'SNARKY',    'TEENAGER',       5847856983, 1485214852, 'IGAVEBIRTH@INYOUTH.EDU',            'USER','PASS');" //19
            , "INSERT INTO users VALUES(NULL, 'TOM',       'CLANCELLBAERRY', 1548965325, 5848569587, 'TOOMANYCRIMES@MYDESK.COM',          'USER','PASS');" //20
            , "INSERT INTO users VALUES(NULL, 'KARI',      'PRISON',         5124845796, 1547854256, 'LOVES@THEROCK.COM',                 'USER','PASS');" //21
            , "INSERT INTO users VALUES(NULL, 'BEAR',      'LONGFELLOW',     1548512457, 2563526589, 'STRETCHES@ARMSTRONG.COM',           'USER','PASS');" //22
            , "INSERT INTO users VALUES(NULL, 'OUTPUT',    'M''DHATASON',    1010010100, 0100100001, 'ONOFFOFFONOFF@SOCKET.COM',          'USER','PASS');" //23
            , "INSERT INTO users VALUES(NULL, 'MESA',      'SNOOP',          3451849765, 3164527548, 'HEALTHYLIVINGNOREALLY@FAREAST.NET', 'USER','PASS');" //24
            , "INSERT INTO users VALUES(NULL, 'GUYE',      'CLARKSON',       5431642758, 1245679185, 'LATER@THEBLUEBIRDCAFE.COM',         'USER','PASS');" //25
            , "INSERT INTO users VALUES(NULL, 'CHARLES',   'BARNES',         6645122436, 5487548151, 'LOVEINANELEVATAAA@AEROLIFT.COM',    'USER','PASS');" //26
            , "INSERT INTO users VALUES(NULL, 'COUNTESS',  'CHOCULA',        3164527548, 6664516666, 'INEVERLEARNEDTOREAD@THATSWHY.COM',  'USER','PASS');" //27
            , "INSERT INTO users VALUES(NULL, 'PRINCE',    'CHARLES',        1542658745, 4854558562, 'FAFAFAFA@DEARY.COM',                'USER','PASS');" //28
            , "INSERT INTO users VALUES(NULL, 'PRINCESS',  'MONOKE',         2569656965, 5525478845, 'IAMAPRINCESS@INJAPAN.COM',          'USER','PASS');" //29

            /* THE FIRST 5 USERS ARE AN ADMIN(1) AND 4 COACHES
            * PLAYER_ID AUTOINCREMENTED, FIRST, LAST, USER_ID (WHICH SHOULD BE THEIR PARENT OR GUARDIAN OR THEMSELVES) */
            , "INSERT INTO player VALUES(NULL, 'MARY',     'ORAZEM',     6);"
            , "INSERT INTO player VALUES(NULL, 'JACK',     'KAROAK',     7);"
            , "INSERT INTO player VALUES(NULL, 'PATRICK',  'KEARNEY',    8);"
            , "INSERT INTO player VALUES(NULL, 'KATHERINE','CURRIN',     9);"
            , "INSERT INTO player VALUES(NULL, 'MARY',     'ORAZEM',    10);"
            , "INSERT INTO player VALUES(NULL, 'JACK',     'KAROAK',    11);"
            , "INSERT INTO player VALUES(NULL, 'PATRICK',  'KEARNEY',   12);"
            , "INSERT INTO player VALUES(NULL, 'DORA',     'CURRIN',    13);"
            , "INSERT INTO player VALUES(NULL, 'MIKE',     'ORAZEM',    14);"
            , "INSERT INTO player VALUES(NULL, 'JAKE',     'KAREOKE',   15);"
            , "INSERT INTO player VALUES(NULL, 'PATRICIA', 'RANEY',     16);"
            , "INSERT INTO player VALUES(NULL, 'LADY',     'DURIN',     17);"
            , "INSERT INTO player VALUES(NULL, 'JAMES',    'DORAZ',     18);"
            , "INSERT INTO player VALUES(NULL, 'JON',      'KAYAK',     19);"
            , "INSERT INTO player VALUES(NULL, 'DICK',     'KEAN',      20);"
            , "INSERT INTO player VALUES(NULL, 'MANNY',    'CURRIN',    21);"
            , "INSERT INTO player VALUES(NULL, 'BARRY',    'LONGFELLOW',22);"
            , "INSERT INTO player VALUES(NULL, 'INPUT',    'M''DHATA',  23);"
            , "INSERT INTO player VALUES(NULL, 'MESO',     'SAHOOP',    24);"
            , "INSERT INTO player VALUES(NULL, 'GUY',      'CLARK',     25);"
            , "INSERT INTO player VALUES(NULL, 'CHERRIE',  'BERRY',     26);"
            , "INSERT INTO player VALUES(NULL, 'COUNT',    'CHOCULA',   27);"
            , "INSERT INTO player VALUES(NULL, 'COOKIE',   'MONSTER',   28);"
            , "INSERT INTO player VALUES(NULL, 'DA-DA',    'BAHAM',     29);"

            /* ENROLLMENT_ID AUTOINCREMENTED,    UID,PID,T_ID, LEAGUE_ID, DATE_ADDED, FEE */

            , "INSERT INTO enrollment VALUES(NULL,  6,  1,  1,  1, '12-APR-2015', 1.99);"
            , "INSERT INTO enrollment VALUES(NULL,  7,  2,  1,  1, '03-MAR-2016', 1.99);"
            , "INSERT INTO enrollment VALUES(NULL,  8,  3,  2,  1, '03-MAR-2016', 1.99);"
            , "INSERT INTO enrollment VALUES(NULL,  9,  4,  2,  1, '03-MAR-2016', 1.99);"
            , "INSERT INTO enrollment VALUES(NULL, 10,  5,  3,  1, '03-MAR-2016', 1.99);"
            , "INSERT INTO enrollment VALUES(NULL, 11,  6,  3,  1, '04-MAR-2016', 1.99);"
            , "INSERT INTO enrollment VALUES(NULL, 12,  7,  4,  1, '01-MAR-2016', 1.99);"
            , "INSERT INTO enrollment VALUES(NULL, 13,  8,  4,  1, '12-APR-2015', 1.99);"
            , "INSERT INTO enrollment VALUES(NULL, 14,  9,  5,  1, '03-MAR-2016', 1.99);"
            , "INSERT INTO enrollment VALUES(NULL, 15, 10,  5,  1, '03-MAR-2016', 1.99);"
            , "INSERT INTO enrollment VALUES(NULL, 16, 11,  6,  1, '03-MAR-2016', 1.99);"
            , "INSERT INTO enrollment VALUES(NULL, 17, 12,  6,  1, '03-MAR-2016', 1.99);"
            , "INSERT INTO enrollment VALUES(NULL, 18, 13,  7,  1, '03-MAR-2016', 1.99);"
            , "INSERT INTO enrollment VALUES(NULL, 19, 14,  8,  1, '03-MAR-2016', 1.99);"
            , "INSERT INTO enrollment VALUES(NULL, 20, 15,  8,  1, '03-MAR-2016', 1.99);"
            , "INSERT INTO enrollment VALUES(NULL, 21, 16,  9,  1, '03-MAR-2016', 1.99);"
            , "INSERT INTO enrollment VALUES(NULL, 22, 17,  9,  1, '03-MAR-2016', 1.99);"
            , "INSERT INTO enrollment VALUES(NULL, 23, 18, 10,  1, '03-MAR-2016', 1.99);"
            , "INSERT INTO enrollment VALUES(NULL, 24, 19, 10,  1, '03-MAR-2016', 1.99);"
            , "INSERT INTO enrollment VALUES(NULL, 25, 20, 11,  1, '03-MAR-2016', 1.99);"
            , "INSERT INTO enrollment VALUES(NULL, 26, 21, 12,  1, '03-MAR-2016', 1.99);"
            , "INSERT INTO enrollment VALUES(NULL, 27, 22, 12,  1, '03-MAR-2016', 1.99);"
            , "INSERT INTO enrollment VALUES(NULL, 28, 23, 13,  1, '03-MAR-2016', 1.99);"
            , "INSERT INTO enrollment VALUES(NULL, 29, 24, 13,  1, '03-MAR-2016', 1.99);"

            /*had to add the coaches to the enrollment table late !!!! I FORGOT TO  !!!!! NO FREEDLOADERS !!! FEAR THE YETI!!!!!*/
/*
            * , "INSERT INTO users VALUES(NULL, 'COUNT', 'BASSY'   , 2548752145, 5326547852, 'BANGSOUNDTHORUGHD@WINDA.NET', 'COACH', 'PASS');"
            , "INSERT INTO users VALUES(NULL, 'SAM'  , 'NEIL'    , 9191594896, 1548693584, 'JURASSIC@PARK.AAH'          , 'COACH', 'PASS');"
            , "INSERT INTO users VALUES(NULL, 'PAT'  , 'COX'     , 9196934563, 7645378909, 'PAT.COX@MINEFIELDBIRD.NET'  , 'COACH', 'PASS');"
            , "INSERT INTO users VALUES(NULL, 'MIKE' , 'KERTCHER', 9587463784, 9588867754, 'KERTCHDADDY@SKINSONTOP.COM' , 'COACH', 'PASS');"
            * */
            , "INSERT INTO enrollment VALUES(NULL,  2,  0,  1,  1,  '01-MAR-2016', 0.00);"
            , "INSERT INTO enrollment VALUES(NULL,  3,  0,  2,  1,  '01-MAR-2016', 0.00);"
            , "INSERT INTO enrollment VALUES(NULL,  4,  0,  1,  2,  '01-MAR-2016', 0.00);"
            , "INSERT INTO enrollment VALUES(NULL,  5,  0,  2,  2,  '01-MAR-2016', 0.00);"

            /* PLACE_ID AUTOINCREMENT, PLACE_NAME, STREET_ADDRESS, CITY, STATE, ZIP */
            , "INSERT INTO place VALUES(NULL, 'HIX COMPLEX', '313 E. SPRING STREET', 'OXFORD', 'NC', '27565' );"
            , "INSERT INTO place VALUES(NULL, 'DIAMOND AT OXFORD PARK', '6048 HORNER SIDING ROAD', 'OXFORD', 'NC', '27565');"
            , "INSERT INTO place VALUES(NULL, 'LAKE DEVIN', 'LAKE DEVIN ROAD', 'OXFORD', 'NC', '27565');"
            , "INSERT INTO place VALUES(NULL, 'GRASSY CREEK RECREATION AREA AT KERR LAKE', '3169 HARRY DAVIS ROAD', 'BULLOCK', 'NC', '27507');"

            /* EVENT_ID, EVENT_TYPE, START_DATE_TIME, PLACE_ID, HOME_TEAM_ID, AWAY_TEAM_ID */
            , "INSERT INTO event VALUES(NULL, 'PRACTICE', '2016/03/27 17:00:00', 2, 3,    1);"
            , "INSERT INTO event VALUES(NULL, 'PRACTICE', '2016/03/28 19:00:00', 1, 2, NULL);"
            , "INSERT INTO event VALUES(NULL, 'GAME',     '2016/03/26 14:00:00', 1, 2,    1);"
            , "INSERT INTO event VALUES(NULL, 'GAME',     '2016/03/23 17:00:00', 1, 1,    2);"
    };
}

DELETE FROM PUBLIC.TRANSACTION;
DELETE FROM PUBLIC.BLOCK;
DELETE FROM PUBLIC.VERSION;
delete from PUBLIC.transaction_shard_index;
delete from public.block_index;
delete from FTL.INDEXES;
delete from public.option;
delete from public.shard;

INSERT INTO PUBLIC.BLOCK
(DB_ID,         ID,                HEIGHT,      VERSION,   TIMESTAMP,  PREVIOUS_BLOCK_ID,  TOTAL_AMOUNT,        TOTAL_FEE,   PAYLOAD_LENGTH,   PREVIOUS_BLOCK_HASH,                                                   CUMULATIVE_DIFFICULTY,  BASE_TARGET,    NEXT_BLOCK_ID,               GENERATION_SIGNATURE,                                                   BLOCK_SIGNATURE,                                                                                                                        PAYLOAD_HASH,                                                           GENERATOR_ID,       TIMEOUT) VALUES
(1641714	,-6206981717632723220  ,15456 	    ,4	        ,142195     , 8306616486060836520	,0	            ,200000000	        ,207	    ,X'a8460f09af074773186c58688eb29215a81d5b0b10fc9e5fc5275b2f39fd93bb'	,X'02dfb519fc012db3'	,23058430050	,-4166853316012435358		,X'df545469ed5a9405e0ff6efcdf468e61564776568c8b227f776f24c47206af46'	,X'3d1c22000eb41599cb12dfbfaa3980353fa84cdf99145d1fcc92886551044a0c0b388c539efa48414c21251e493e468d97a2df12be24e9a33dec4521fdb6c2eb'	,X'550dfe6da8732c1977c7545675f8dc163995aaba5533306b7a1f1b9364190dd3'	, 3883484057046974168	,0   ),
(1641715	,-4166853316012435358  ,104595 	    ,6	        ,962274     ,-6206981717632723220	,0	            ,0	                ,0	        ,X'ec562889035fdca9d59d9bdca460992c01c5286278104287a989834eeffcb83e'	,X'02dfb51a2bb035b4'	,23058430050	, 433871417191886464		,X'82e59d851fdf0d01ca1ee20df906009cd66885cc63e8314ebde80dc5e38987fa'	,X'202acda4d57f2a24212d265053241a07608de29a6dd8252994cf8be197765d02a585c676aca15e7f43a57d7747173d51435d9f2820da637ca8bc9cd1e536d761'	,X'e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855'	, 3883484057046974168	,9   ),
(1650020	,6282714800700403321   ,517468	    ,5	        ,41571157	,-3194395162061405253	,12000000000    ,23000000000	    ,414	    ,X'bb831a55863aabd3d2622a1692a4c03ba9eb14839902e029a702c58aeea6a935'	,X'3d46b0302ef95c'      ,7686143350	    ,-5966687593234418746       ,X'd60150d67b47f37a90ca0b0c7a0151af1c2d9a69687f3eef75f42d7b5f12c191'	,X'd2c6b60abaf85e17f65f339879fda8de5346415908a9cbb9a21b3c6d24bd1d0454222fb8962ad2aec679da0d8fb7e835b76a35301c33e925b48245a9d24954de'	,X'4555a1d9a7c2226b9a5797e56d245485cb94fdb2495fc8ca31c3297e597c7b68'	,9211698109297098287	,2   ),
(1800000	,-5966687593234418746	,553326	    ,3	        ,41974339	,-420771891665807004	,0	            ,1000000000	        ,2668	    ,X'6459caa1311e29fa9c60bed5752f161a5e82b77328cac949cb7afbaccacfbb8e'	,X'3de7206ceaebce'	    ,168574215	    , null              	    ,X'dc3b7c24f1e6caba84e39ff7b8f4040be4c614b16b7e697364cedecdd072b6df'	,X'866847568d2518e1c1c6f97ee014b6f15e4197e5ff9041ab449d9087aba343060e746dc56dbc34966d42f6fd326dc5c4b741ae330bd5fa56539022bd75643cd6'	,X'cf8dc4e015626b309ca7518a390e3e1e7b058a83428287ff39dc49b1518df50c'	,9211698109297098287	,0),
  ;
INSERT INTO PUBLIC.TRANSACTION
(DB_ID,     ID,                      HEIGHT,      BLOCK_ID,            BLOCK_TIMESTAMP,    DEADLINE, RECIPIENT_ID,     TRANSACTION_INDEX, AMOUNT,             FEE,            FULL_HASH,                                                                     SIGNATURE,                                                                                                                                  TIMESTAMP, TYPE, SUBTYPE, SENDER_ID,                SENDER_PUBLIC_KEY,                                                   REFERENCED_TRANSACTION_FULL_HASH,                                                     PHASED, VERSION, HAS_MESSAGE, HAS_ENCRYPTED_MESSAGE, HAS_PUBLIC_KEY_ANNOUNCEMENT, EC_BLOCK_HEIGHT,   EC_BLOCK_ID,            HAS_ENCRYPTTOSELF_MESSAGE, HAS_PRUNABLE_MESSAGE, HAS_PRUNABLE_ENCRYPTED_MESSAGE, HAS_PRUNABLE_ATTACHMENT, ATTACHMENT_BYTES) VALUES
  (3500     ,-2262365651675616510	  ,15456	 ,-6206981717632723220      ,142195        ,1440,	2569665864951373924	    ,0	        ,100000000000000000	,100000000	    ,X'026bd4236d769ae022df97e248c6292aef1f403f5d5dcb74d787255344cf58e5'	      ,X'1a3ecfc672df4ae91b1bcf319cee962426cd3f65fac340a0e01ac27367646904fa8ccf22f0b0c93f84d00584fa3f7f5bd03933e08b3aa1295a9ebdd09a0c1654'	    ,35078473	    ,0	   ,0	    ,9211698109297098287	 ,X'bf0ced0472d8ba3df9e21808e98e61b34404aad737e2bae1778cebc698b40f37' ,X'863e0c0752c6380be76354bd861be0705711e0ee2bc0b84d9f0d71b5a4271af6'                  ,FALSE		,1	,FALSE	        ,FALSE	            ,FALSE	                        ,14734	        ,2621055931824266697	,FALSE	                    ,FALSE	                ,FALSE	                        ,FALSE,                 null),
  (4000     ,9145605905642517648	  ,15456	 ,-6206981717632723220      ,142195        ,1440,	2230095012677269409	    ,1	        ,100000000000000000	,100000000	    ,X'9074899d1db8eb7e807f0d841973fdc8a84ab2742a4fb03d47b620f5e920e5fe'	      ,X'6ae95b4165ef53b335ac576a72d20d24464f57bd49dbdd76dd22f519caff3d0457d97769ae76d8496906e4f1ab5f7db30db73daea5db889d80e1ac0bd4b05257'	    ,35078474	    ,0	   ,0	    ,-8315839810807014152	 ,X'bf0ced0472d8ba3df9e21808e98e61b34404aad737e2bae1778cebc698b40f37' ,null	                                                                                ,FALSE		,1	,FALSE	        ,FALSE	            ,FALSE	                        ,14734	        ,2621055931824266697	,FALSE	                    ,FALSE	                ,FALSE	                        ,FALSE,                 null),
  (4500     ,-1536976186224925700     ,15456	 ,-6206981717632723220      ,142195    	   ,1440,	null	                ,2	        ,0	                ,100000000	    ,X'fc23d4474d90abeae5dd6d599381a75a2a06e61f91ff2249067a10e6515d202f'	      ,X'61a224ae2d8198bfcee91c83e449d6325a2caa974f6a477ab59d0072b9b7e50793575534ab29c7be7d3dbef46f5e9e206d0bf5801bebf06847a28aa16c6419a1'	    ,36758888	    ,8	   ,1	    ,9211698109297098287	 ,X'bf0ced0472d8ba3df9e21808e98e61b34404aad737e2bae1778cebc698b40f37' ,X'830fc103e1cc5bc7a3dd989ad0d7a8c66307a9ef23f05d2a18b661ee0a464088'	                ,FALSE      ,1	,FALSE	        ,FALSE	            ,FALSE	                        ,103874	        ,1281949812948953897	,FALSE	                    ,FALSE	                ,FALSE	                        ,FALSE,	                X'01054c494e5558035838360002a427a6d86645d0c32527e50fe292a0b1cf3983ef083f9fc392359e34d90012a65d5bd927c2cd09466433c107e523ff01bc00e414108d01e515f56ddbc054abce83fa4bd30bdf4623928e768536f8e56d9695ebadfbe34b5d1d59aa63545f5238a4817ec09389687df5ec116423b0e572a5ee9c47eaab432b19805a610beecb495595636a14009524caee8f1c73db084f1842bf895440233bff67c8f09674056113efd58da69f8411df3df174438bd2e8280e4eac97d6f89a6d756c1feddccc6d593d59578aab46ad9024b0ba742c547418ea7b2adbed80c8f673cd2cff31fefb6ab068c03232d79dfd83977a05bb0fb286f81ddbc0a9c75e6fce81747223a8fe5e506f9a9d7a7fd08d51b63ba25b4872886857b59607e24e842aa39e9d0d78a3db3ad97b03e64fb135ef55f5f396e29c8a4e146087b853f9a1be0a647201836da32ef5b0bff1a3bc599bff155cbfe8a24ad5ee7ab711bf9de7682876c8b7986025e68c8ee63f63505d3ec21f53a98e9de78f39b69c8438028a0e569f81c9ac7bc7d2dc0ea4f4406a696938fe422bad1076342267ee13d657aa9e68d07aafba6b33fc3e90d72ea5147bc21d223b862c56d989a568a7a2609b272261df3af318f340283490ff4d909768deee8987e363bba10c489d746e4e706daf02b78ba5886f59c204bc2237702d1c2191a6c6b0d3095c9c3d462e4e1cae02f0f53b5e94c2150002b51c553a2e69bc868926235c2fc01ba04b69070324a0c94d9c0d32f65ad4bb475c2b2887800caed2f4023f6510c363a5c4a7da0d8ba7cf85e921990fa7eba87c053ee753157c7541b291483a3f444b0e5d91dcb0f74def9dbe46c910546d0b616ebd9241e7f09aa619cb84b95560307d7e6b07e4fa47c508a621683717485542883203f1f17279b5e93173fa01b19bc707b1ee899bd1118322befed65b6eb28df579d56e61ca6b90abe5408f21544e3e6195ab23876baab07db967de04e815a9395987775acbe57bb7ac8d7366ad62a655bb4598edb4d3d2dce3d326fbeef97b654c686e9abd2c613ea740701a5a4d647e1ebf3bda0fc29fdbb5dfc7dc22842f32e552b0f999076d5f644809ff752224b71fe2f85ad8ac4766d57756d52953bbfb6e6b2134b173bf4995218429371ce3989cd764482396acb05eeaf2e138f38bae9107a9b6db626c6647be5d4a1e6f02f17326700ddeec0b8037671252f0e5c475e06964b6c5a5ff51bc07b494ee84ef5be7d84146f949fe6639409c3fe7550597e45c93ec276721781d9e8677fe4501b583a2b6d96d583c6397c8c5ef14ab6932581d81a8a3518da882fb920dd47c4af25ed755697a7cb181936ae0f21f3c2976f3168202e02fc4b351dcbb7f0c9e5b50a7f1f1d1841dd4de09ca374e3d01fc4fa6cb9271c727a194a2b701ec5e7d882790bb800cc2f86339ad708869ea291105312e302e382000a2c1e47afd4b25035a025091ec3c33ec1992d09e7f3c05875d79e660139220a4'),
  (5000     ,-4081443370478530685	  ,15456	 ,-6206981717632723220      ,142195        ,1440,	null	                ,3	        ,0	                ,100000000	    ,X'830fc103e1cc5bc7a3dd989ad0d7a8c66307a9ef23f05d2a18b661ee0a464088'	      ,X'551f99bc4eceaae7c7007ac077ed163f4d95f8acc0119e38b726b5c8b494cf09c5059292de17efbc4ec14848e3944ecd0a5d0ca2591177266e04d426ce25a1c1'      ,36763004	    ,8	   ,0	    ,9211698109297098287	 ,X'bf0ced0472d8ba3df9e21808e98e61b34404aad737e2bae1778cebc698b40f37' ,null	                                                                                ,FALSE      ,1	,FALSE	        ,FALSE	            ,FALSE	                        ,103950	        ,3234042379296483074	,FALSE	                    ,FALSE	                ,FALSE	                        ,FALSE,	                X'01054c494e555805414d4436340002a427a6d86645d0c32527e50fe292a0b1cf3983ef083f9fc392359e34d90012a65d5bd927c2cd09466433c107e523ff01bc00e414108d01e515f56ddbc054abce83fa4bd30bdf4623928e768536f8e56d9695ebadfbe34b5d1d59aa63545f5238a4817ec09389687df5ec116423b0e572a5ee9c47eaab432b19805a610beecb495595636a14009524caee8f1c73db084f1842bf895440233bff67c8f09674056113efd58da69f8411df3df174438bd2e8280e4eac97d6f89a6d756c1feddccc6d593d59578aab46ad9024b0ba742c547418ea7b2adbed80c8f673cd2cff31fefb6ab068c03232d79dfd83977a05bb0fb286f81ddbc0a9c75e6fce81747223a8fe5e506f9a9d7a7fd08d51b63ba25b4872886857b59607e24e842aa39e9d0d78a3db3ad97b03e64fb135ef55f5f396e29c8a4e146087b853f9a1be0a647201836da32ef5b0bff1a3bc599bff155cbfe8a24ad5ee7ab711bf9de7682876c8b7986025e68c8ee63f63505d3ec21f53a98e9de78f39b69c8438028a0e569f81c9ac7bc7d2dc0ea4f4406a696938fe422bad1076342267ee13d657aa9e68d07aafba6b33fc3e90d72ea5147bc21d223b862c56d989a568a7a2609b272261df3af318f340283490ff4d909768deee8987e363bba10c489d746e4e706daf02b78ba5886f59c204bc2237702d1c2191a6c6b0d3095c9c3d462e4e1cae02f0f53b5e94c2150002b51c553a2e69bc868926235c2fc01ba04b69070324a0c94d9c0d32f65ad4bb475c2b2887800caed2f4023f6510c363a5c4a7da0d8ba7cf85e921990fa7eba87c053ee753157c7541b291483a3f444b0e5d91dcb0f74def9dbe46c910546d0b616ebd9241e7f09aa619cb84b95560307d7e6b07e4fa47c508a621683717485542883203f1f17279b5e93173fa01b19bc707b1ee899bd1118322befed65b6eb28df579d56e61ca6b90abe5408f21544e3e6195ab23876baab07db967de04e815a9395987775acbe57bb7ac8d7366ad62a655bb4598edb4d3d2dce3d326fbeef97b654c686e9abd2c613ea740701a5a4d647e1ebf3bda0fc29fdbb5dfc7dc22842f32e552b0f999076d5f644809ff752224b71fe2f85ad8ac4766d57756d52953bbfb6e6b2134b173bf4995218429371ce3989cd764482396acb05eeaf2e138f38bae9107a9b6db626c6647be5d4a1e6f02f17326700ddeec0b8037671252f0e5c475e06964b6c5a5ff51bc07b494ee84ef5be7d84146f949fe6639409c3fe7550597e45c93ec276721781d9e8677fe4501b583a2b6d96d583c6397c8c5ef14ab6932581d81a8a3518da882fb920dd47c4af25ed755697a7cb181936ae0f21f3c2976f3168202e02fc4b351dcbb7f0c9e5b50a7f1f1d1841dd4de09ca374e3d01fc4fa6cb9271c727a194a2b701ec5e7d882790bb800cc2f86339ad708869ea291105312e302e382000a2c1e47afd4b25035a025091ec3c33ec1992d09e7f3c05875d79e660139220a4'),
  (6000 	,4851834545659781120	  ,517468    ,6282714800700403321       ,41571157      ,1440,   7477442401604846627	    ,0	        ,12000000000	    ,23000000000	,X'0020052bd02d5543c4408aed90d98e636fdb21447cbed0c1f1e2db3134e37fbf'		  ,X'7ace0ea75778aebb8363e141da74b4efce571dc73c728de7f3bcd6126fe3ab04fb1b8e3170e6fe4e458f9fd40f8d10ef7bc8caa839ae9c28a2276f02ddccd2ff'	    ,41571172	    ,0	   ,0	    ,9211698109297098287	 ,X'bf0ced0472d8ba3df9e21808e98e61b34404aad737e2bae1778cebc698b40f37' ,null	                                                                                ,TRUE		,1	,TRUE	        ,FALSE	            ,FALSE	                        ,516746	        ,5629144656878115682	,TRUE	                    ,FALSE	                ,TRUE	                        ,FALSE,                 X'010c00008054657374206d65737361676501400000808bb31f0eb60af644d69bad77c5158ceac89bb3b02856542f334de903be92ad354d11f1f5eb876d3e558c40513c813248a879751d03d6446d6c562e04306573f6adcb4a9238585b1f9f1df4c124055da5ba78d76521eb2ace178f552d064a2cf802a83108000232000000000000000a0000000000000002dc3fd47da87a5620983fe492a3968c6c93931ffe397ff94202000000ffffffff019fec636832fa9108934bac4902b7bd9213f4c0f073625dcdc9a2c511cc715fdc'),
  (7000	    ,9175410632340250178	  ,553326	 ,-5966687593234418746	    ,41974339	   ,1440,	null	                ,0	        ,0	                ,1000000000	    ,X'429efb505b9b557f5d2a1d6d506cf75de6c3692ca1a21217ae6160c7658c7312'	      ,X'7ecae5825a24dedc42dd11e2239ced7ad797c6d6c9aedc3d3275204630b7e20832f9543d1063787ea1f32ab0993ea733aa46a52664755d9e54f211cdc3c5c5fd'	    ,41974329	    ,3	   ,0	    ,3705364957971254799	 ,X'39dc2e813bb45ff063a376e316b10cd0addd7306555ca0dd2890194d37960152' ,null	                                                                                ,FALSE	    ,1	,FALSE	        ,FALSE	            ,FALSE	                        ,552605	        ,4407210215527895706	,FALSE	                    ,TRUE	                ,FALSE	                        ,FALSE,	                X'010c00546573742070726f647563741500546573742070726f6475637420666f722073616c650c007461672074657374646174610200000000e40b540200000001b9dd15475e2f8da755f1b63933051dede676b223c86e70f54c7182b976d2f86d'),
;


INSERT into PUBLIC.BLOCK_INDEX (block_id, block_height) VALUES
(-107868771406622438 , 0	 ),
(-468651855371775066 , 2994  ),
(-7242168411665692630, 2995  ),
(-6746699668324916965, 2996  ),
(-3540343645446911906, 2997  ),
( 2729391131122928659, 2998  ),
( 1842732555539684628, 3500  ),
(-5580266015477525080, 5000  ),
( 6438949995368593549, 8000  ),
( 7551185434952726924, 10000 ),
( 8306616486060836520, 15455 ),
;
INSERT into Public.TRANSACTION_SHARD_INDEX(transaction_id, partial_transaction_hash  ,height     ,transaction_index) VALUES
(3444674909301056677        ,X'cc6f17193477209ca5821d37d391e70ae668dd1c11dd798e'     ,   1000    ,0  ),
(2402544248051582903        ,X'2270a2b00e3f70fb5d5d8e0da3c7919edd4d3368176e6f2d'     ,   1000    ,1  ),
(5373370077664349170        ,X'b96d5e9f64e51c597513717691eeeeaf18a26a864034f62c'     ,   1500    ,0  ),
(-780794814210884355        ,X'cca5a1f825f9b918be00f35406f70b108b6656b299755558'     ,   2000    ,0  ),
(-9128485677221760321       ,X'fc18147b1d9360b4ae06fc65905948fbce127c302201e9a1'     ,   3500    ,0  ),
(3746857886535243786        ,X'3fc7b055930adb2997b5fffaaa2cf86fa360fe235311e9d3'     ,   3500    ,1  ),
(5471926494854938613        ,X'01c5a131b52099356c899b29addb0476d835ea2de5cc5691'     ,   3500    ,2  ),
(2083198303623116770        ,X'6ee735c9da0d55af7100c45263a0a6a0920c255a0f65b44f'     ,   8000    ,0  ),
(808614188720864902	        ,X'e76354bd861be0705711e0ee2bc0b84d9f0d71b5a4271af6'     ,   8000    ,1  ),
(100                        ,X'e76354bd861be0705711e0ee2bc0b84d9f0d71b5a4271af1'     ,   400000  ,0  ),
;
INSERT into PUBLIC.SHARD (shard_id, shard_state, shard_hash, shard_height, zip_hash_crc, generator_ids) VALUES
(1, 100, X'8dd2cb2fcd453c53b3fe53790ac1c104a6a31583e75972ff62bced9047a15176', 2998, null , (4821792282200,)),
(2, 100, X'a3015d38155ea3fd95fe8952f579791e4ce7f5e1e21b4ca4e0c490553d94fb7d', 15456, null, (7821792282123976600,)),
(3, 99, X'a3015d38155ea3fd95fe8952f579791e4ce7f5e1e21b4ca4e0c490553d94fb7d', 500000, null, (57821792282, 22116981092100, 9211698109297098287,)),
;

INSERT into version values (279);

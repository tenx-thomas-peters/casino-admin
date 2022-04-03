$("#saveGlobalconfig").click(function(){
			var baccarat_check_json = {};
    			baccarat_check_json = {
    				"evolution": 	$("input[name='evolution']:checked").val(),
    				"olvet": 	 	$("input[name='olvet']:checked").val(),
    				"dreamGaming":  $("input[name='dreamGaming']:checked").val(),
    				"microgaming":  $("input[name='microgaming']:checked").val(),
    				"sexygaming":   $("input[name='sexygaming']:checked").val(),
    				"asiaGaming":   $("input[name='asiaGaming']:checked").val(),
    				"orientalPlus": $("input[name='orientalPlus']:checked").val(),
    				"bbin":         $("input[name='bbin']:checked").val(),
    				"vivoGaming":   $("input[name='vivoGaming']:checked").val(),
    				"pragmatic":    $("input[name='pragmatic']:checked").val(),
    				"taishan":      $("input[name='taishan']:checked").val(),
    				"lotus":        $("input[name='lotus']:checked").val(),
    				"vmCasino":     $("input[name='vmCasino']:checked").val()
    			}
			$("input[name='baccaratCheck']").val(JSON.stringify(baccarat_check_json));

			var slot_check_json = {};
    			slot_check_json = {
    				"vmCasino2": 		$("input[name='vmCasino2']:checked").val(),
    				"programmaticPlay": $("input[name='programmaticPlay']:checked").val(),
    				"booungo": 			$("input[name='booungo']:checked").val(),
    				"realTimeGaming":   $("input[name='realTimeGaming']:checked").val(),
    				"bbTech":  			$("input[name='bbTech']:checked").val(),
    				"microgaming1":     $("input[name='microgaming1']:checked").val(),
    				"asiaGaming1":      $("input[name='asiaGaming1']:checked").val(),
    				"habanero":         $("input[name='habanero']:checked").val(),
    				"evoplay":   		$("input[name='evoplay']:checked").val(),
    				"playstar":   		$("input[name='playstar']:checked").val(),
    				"gameart":     		$("input[name='gameart']:checked").val(),
    				"genesis":       	$("input[name='genesis']:checked").val(),
    				"topTrend":     	$("input[name='topTrend']:checked").val(),
    				"stargame":     	$("input[name='stargame']:checked").val(),
    				"nonGaming":    	$("input[name='nonGaming']:checked").val(),
    				"dreamTech":    	$("input[name='dreamTech']:checked").val(),
    				"benesoft":    		$("input[name='benesoft']:checked").val(),
    				"fijiSoft":    		$("input[name='fijiSoft']:checked").val(),
    				"tpg":    			$("input[name='tpg']:checked").val(),
    				"playengo":     	$("input[name='playengo']:checked").val(),
    				"aritocrat":    	$("input[name='aritocrat']:checked").val(),
    				"playson":     		$("input[name='playson']:checked").val()

    			}
			$("input[name='slotCheck']").val(JSON.stringify(slot_check_json));
    		
			$("input[name='serviceInspectionCheck']").val($("input[name='serviceInspection']").val());
			$("input[name='recommenderAtTheTimeOfMemberRegistration']").val($("input[name='inputRequired']:checked").val());


		    var bankAccount_json = {};
			bankAccount_json = {
				"nameOfBank": 	    $("input[name='nameOfBank']").val(),
				"accountNumber": 	$("input[name='accountNumber']").val(),
				"accountHolder": 	$("input[name='accountHolder']").val()
			}
			$("input[name='bankAccount']").val(JSON.stringify(bankAccount_json));
		});

		$(document).ready(function(){
			var baccaratCheckObj = JSON.parse($("input[name='baccaratCheck']").val());
					$("input[name='evolution'][value='" + baccaratCheckObj.evolution + "']").attr("checked", "checked");
					$("input[name='olvet'][value='" + baccaratCheckObj.olvet + "']").attr("checked", "checked");
					$("input[name='dreamGaming'][value='" + baccaratCheckObj.dreamGaming + "']").attr("checked", "checked");
					$("input[name='microgaming'][value='" + baccaratCheckObj.microgaming + "']").attr("checked", "checked");
					$("input[name='sexygaming'][value='" + baccaratCheckObj.sexygaming + "']").attr("checked", "checked");
					$("input[name='asiaGaming'][value='" + baccaratCheckObj.asiaGaming + "']").attr("checked", "checked");
					$("input[name='orientalPlus'][value='" + baccaratCheckObj.orientalPlus + "']").attr("checked", "checked");
					$("input[name='bbin'][value='" + baccaratCheckObj.bbin + "']").attr("checked", "checked");
					$("input[name='vivoGaming'][value='" + baccaratCheckObj.vivoGaming + "']").attr("checked", "checked");
					$("input[name='pragmatic'][value='" + baccaratCheckObj.pragmatic + "']").attr("checked", "checked");
					$("input[name='taishan'][value='" + baccaratCheckObj.taishan + "']").attr("checked", "checked");
					$("input[name='lotus'][value='" + baccaratCheckObj.lotus + "']").attr("checked", "checked");
					$("input[name='vmCasino'][value='" + baccaratCheckObj.vmCasino + "']").attr("checked", "checked");

			var slotCheckObj = JSON.parse($("input[name='slotCheck']").val());
					$("input[name='vmCasino2'][value='" + slotCheckObj.vmCasino2 + "']").attr("checked", "checked");
					$("input[name='programmaticPlay'][value='" + slotCheckObj.programmaticPlay + "']").attr("checked", "checked");
					$("input[name='booungo'][value='" + slotCheckObj.booungo + "']").attr("checked", "checked");
					$("input[name='realTimeGaming'][value='" + slotCheckObj.realTimeGaming + "']").attr("checked", "checked");
					$("input[name='bbTech'][value='" + slotCheckObj.bbTech + "']").attr("checked", "checked");
					$("input[name='microgaming1'][value='" + slotCheckObj.microgaming1 + "']").attr("checked", "checked");
					$("input[name='asiaGaming1'][value='" + slotCheckObj.asiaGaming1 + "']").attr("checked", "checked");
					$("input[name='habanero'][value='" + slotCheckObj.habanero + "']").attr("checked", "checked");
					$("input[name='evoplay'][value='" + slotCheckObj.evoplay + "']").attr("checked", "checked");
					$("input[name='playstar'][value='" + slotCheckObj.playstar + "']").attr("checked", "checked");
					$("input[name='gameart'][value='" + slotCheckObj.gameart + "']").attr("checked", "checked");
					$("input[name='genesis'][value='" + slotCheckObj.genesis + "']").attr("checked", "checked");
					$("input[name='topTrend'][value='" + slotCheckObj.topTrend + "']").attr("checked", "checked");
					$("input[name='stargame'][value='" + slotCheckObj.stargame + "']").attr("checked", "checked");
					$("input[name='nonGaming'][value='" + slotCheckObj.nonGaming + "']").attr("checked", "checked");
					$("input[name='dreamTech'][value='" + slotCheckObj.dreamTech + "']").attr("checked", "checked");
					$("input[name='benesoft'][value='" + slotCheckObj.benesoft + "']").attr("checked", "checked");
					$("input[name='fijiSoft'][value='" + slotCheckObj.fijiSoft + "']").attr("checked", "checked");
					$("input[name='tpg'][value='" + slotCheckObj.tpg + "']").attr("checked", "checked");
					$("input[name='playengo'][value='" + slotCheckObj.playengo + "']").attr("checked", "checked");
					$("input[name='aritocrat'][value='" + slotCheckObj.aritocrat + "']").attr("checked", "checked");
					$("input[name='playson'][value='" + slotCheckObj.playson + "']").attr("checked", "checked");
					$("input[name='serviceInspection'][value='" + $("input[name='serviceInspectionCheck']").val() + "']").attr("checked", "checked");
					$("input[name='inputRequired'][value='" + $("input[name='recommenderAtTheTimeOfMemberRegistration']").val() + "']").attr("checked", "checked");

			var bankAccountObj = JSON.parse($("input[name='bankAccount']").val());
					$("input[name='nameOfBank']").val(bankAccountObj.nameOfBank);
					$("input[name='accountNumber']").val(bankAccountObj.accountNumber);
					$("input[name='accountHolder']").val(bankAccountObj.accountHolder);
		});

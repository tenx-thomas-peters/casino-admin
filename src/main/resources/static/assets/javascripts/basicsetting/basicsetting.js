$("#saveGlobalconfig").click(function(){
			var baccarat_check_json = {};
    			baccarat_check_json = {
    				"baccarat_evolution": 	$("input[name='baccarat_evolution']:checked").val(),
    				"baccarat_allbet": 	 	$("input[name='baccarat_allbet']:checked").val(),
    				"baccarat_dreamgame":  $("input[name='baccarat_dreamgame']:checked").val(),
    				"baccarat_microgaming":  $("input[name='baccarat_microgaming']:checked").val(),
    				"sexygaming_vivo":   $("input[name='sexygaming_vivo']:checked").val(),
    				"baccarat_asiagaming":   $("input[name='baccarat_asiagaming']:checked").val(),
    				"baccarat_oriental": $("input[name='baccarat_oriental']:checked").val(),
    				"baccarat_bbin":         $("input[name='baccarat_bbin']:checked").val(),
    				"baccarat_vivo":   $("input[name='baccarat_vivo']:checked").val(),
    				"baccarat_pragmatic":    $("input[name='baccarat_pragmatic']:checked").val(),
    				"baccarat_taishan":      $("input[name='baccarat_taishan']:checked").val(),
    				"baccarat_lotus":        $("input[name='baccarat_lotus']:checked").val(),
    				"baccarat_vmcasino":     $("input[name='baccarat_vmcasino']:checked").val()
    			}
			$("input[name='baccaratCheck']").val(JSON.stringify(baccarat_check_json));

			var slot_check_json = {};
    			slot_check_json = {
    				"slot_vmcasino2": 		$("input[name='slot_vmcasino2']:checked").val(),
    				"slot_pragmatic": $("input[name='slot_pragmatic']:checked").val(),
    				"slot_booongo": 			$("input[name='slot_booongo']:checked").val(),
    				"slot_realtime":   $("input[name='slot_realtime']:checked").val(),
    				"slot_bbtech":  			$("input[name='slot_bbtech']:checked").val(),
    				"slot_microgaming":     $("input[name='slot_microgaming']:checked").val(),
    				"slot_asiagaming":      $("input[name='slot_asiagaming']:checked").val(),
    				"slot_habanero":         $("input[name='slot_habanero']:checked").val(),
    				"slot_evoplay":   		$("input[name='slot_evoplay']:checked").val(),
    				"slot_playstar":   		$("input[name='slot_playstar']:checked").val(),
    				"slot_gameart":     		$("input[name='slot_gameart']:checked").val(),
    				"slot_genesis":       	$("input[name='slot_genesis']:checked").val(),
    				"slot_toptrend":     	$("input[name='slot_toptrend']:checked").val(),
    				"slot_stargame":     	$("input[name='slot_stargame']:checked").val(),
    				"slot_bgame":    	$("input[name='slot_bgame']:checked").val(),
    				"slot_dreamtech":    	$("input[name='slot_dreamtech']:checked").val(),
    				"slot_betsoft":    		$("input[name='slot_betsoft']:checked").val(),
    				"slot_pgsoft":    		$("input[name='slot_pgsoft']:checked").val(),
    				"slot_tpg":    			$("input[name='slot_tpg']:checked").val(),
    				"slot_cq9":     	$("input[name='slot_cq9']:checked").val(),
    				"slot_aritocrat":    	$("input[name='slot_aritocrat']:checked").val(),
    				"slot_playson":     		$("input[name='slot_playson']:checked").val()

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
					$("input[name='baccarat_evolution'][value='" + baccaratCheckObj.baccarat_evolution + "']").attr("checked", "checked");
					$("input[name='baccarat_allbet'][value='" + baccaratCheckObj.baccarat_allbet + "']").attr("checked", "checked");
					$("input[name='baccarat_dreamgame'][value='" + baccaratCheckObj.baccarat_dreamgame + "']").attr("checked", "checked");
					$("input[name='baccarat_microgaming'][value='" + baccaratCheckObj.baccarat_microgaming + "']").attr("checked", "checked");
					$("input[name='sexygaming_vivo'][value='" + baccaratCheckObj.sexygaming_vivo + "']").attr("checked", "checked");
					$("input[name='baccarat_asiagaming'][value='" + baccaratCheckObj.baccarat_asiagaming + "']").attr("checked", "checked");
					$("input[name='baccarat_oriental'][value='" + baccaratCheckObj.baccarat_oriental + "']").attr("checked", "checked");
					$("input[name='baccarat_bbin'][value='" + baccaratCheckObj.baccarat_bbin + "']").attr("checked", "checked");
					$("input[name='baccarat_vivo'][value='" + baccaratCheckObj.baccarat_vivo + "']").attr("checked", "checked");
					$("input[name='baccarat_pragmatic'][value='" + baccaratCheckObj.baccarat_pragmatic + "']").attr("checked", "checked");
					$("input[name='baccarat_taishan'][value='" + baccaratCheckObj.baccarat_taishan + "']").attr("checked", "checked");
					$("input[name='baccarat_lotus'][value='" + baccaratCheckObj.baccarat_lotus + "']").attr("checked", "checked");
					$("input[name='baccarat_vmcasino'][value='" + baccaratCheckObj.baccarat_vmcasino + "']").attr("checked", "checked");

			var slotCheckObj = JSON.parse($("input[name='slotCheck']").val());
					$("input[name='slot_vmcasino2'][value='" + slotCheckObj.slot_vmcasino2 + "']").attr("checked", "checked");
					$("input[name='slot_pragmatic'][value='" + slotCheckObj.slot_pragmatic + "']").attr("checked", "checked");
					$("input[name='slot_booongo'][value='" + slotCheckObj.slot_booongo + "']").attr("checked", "checked");
					$("input[name='slot_realtime'][value='" + slotCheckObj.slot_realtime + "']").attr("checked", "checked");
					$("input[name='slot_bbtech'][value='" + slotCheckObj.slot_bbtech + "']").attr("checked", "checked");
					$("input[name='slot_microgaming'][value='" + slotCheckObj.slot_microgaming + "']").attr("checked", "checked");
					$("input[name='slot_asiagaming'][value='" + slotCheckObj.slot_asiagaming + "']").attr("checked", "checked");
					$("input[name='slot_habanero'][value='" + slotCheckObj.slot_habanero + "']").attr("checked", "checked");
					$("input[name='slot_evoplay'][value='" + slotCheckObj.slot_evoplay + "']").attr("checked", "checked");
					$("input[name='slot_playstar'][value='" + slotCheckObj.slot_playstar + "']").attr("checked", "checked");
					$("input[name='slot_gameart'][value='" + slotCheckObj.slot_gameart + "']").attr("checked", "checked");
					$("input[name='slot_genesis'][value='" + slotCheckObj.slot_genesis + "']").attr("checked", "checked");
					$("input[name='slot_toptrend'][value='" + slotCheckObj.slot_toptrend + "']").attr("checked", "checked");
					$("input[name='slot_stargame'][value='" + slotCheckObj.slot_stargame + "']").attr("checked", "checked");
					$("input[name='slot_bgame'][value='" + slotCheckObj.slot_bgame + "']").attr("checked", "checked");
					$("input[name='slot_dreamtech'][value='" + slotCheckObj.slot_dreamtech + "']").attr("checked", "checked");
					$("input[name='slot_betsoft'][value='" + slotCheckObj.slot_betsoft + "']").attr("checked", "checked");
					$("input[name='slot_pgsoft'][value='" + slotCheckObj.slot_pgsoft + "']").attr("checked", "checked");
					$("input[name='slot_tpg'][value='" + slotCheckObj.slot_tpg + "']").attr("checked", "checked");
					$("input[name='slot_cq9'][value='" + slotCheckObj.slot_cq9 + "']").attr("checked", "checked");
					$("input[name='slot_aritocrat'][value='" + slotCheckObj.slot_aritocrat + "']").attr("checked", "checked");
					$("input[name='slot_playson'][value='" + slotCheckObj.slot_playson + "']").attr("checked", "checked");
					$("input[name='serviceInspection'][value='" + $("input[name='serviceInspectionCheck']").val() + "']").attr("checked", "checked");
					$("input[name='inputRequired'][value='" + $("input[name='recommenderAtTheTimeOfMemberRegistration']").val() + "']").attr("checked", "checked");

			var bankAccountObj = JSON.parse($("input[name='bankAccount']").val());
					$("input[name='nameOfBank']").val(bankAccountObj.nameOfBank);
					$("input[name='accountNumber']").val(bankAccountObj.accountNumber);
					$("input[name='accountHolder']").val(bankAccountObj.accountHolder);
		});

require('lib.tlv')

AID_UJF   = "#556E69762E554A46"
function ui_parse_cstring(node,data)
	ui.tree_set_value(node,data)
	if #data>1 then
        	ui.tree_set_alt_value(node,bytes.format("%P",bytes.sub(data,0,#data)))
	end
end
function raw_get_data(id) 
	return card.send(bytes.new(8,card.CLA,0xCA,id,00,01,01,00))
end

if card.connect() then
  CARD = card.tree_startup("UJF")

  sw,resp = card.select(AID_UJF)
  if sw==0x9000 then
	APP = ui.tree_add_node(CARD,"application","Univ.UJF", AID_UJF)

	sw,resp = raw_get_data(0x01)
	if sw==0x9000 then
     		ID = ui.tree_add_node(APP,"id","ID", "01", #resp)
		ui_parse_cstring(ID,resp)
	end

	sw,resp = raw_get_data(0x02)
	if sw==0x9000 then
     		VERSION = ui.tree_add_node(APP,"version","VERSION", "02", #resp)
		ui_parse_cstring(VERSION,resp)
	end

	sw,resp = raw_get_data(0x03)
	if sw==0x9000 then
     		HOLDER = ui.tree_add_node(APP,"holder","HOLDER", "03", #resp)
		if resp[0] == 0x30 then
			ui.tree_set_alt_value(HOLDER, "Student")		
		elseif resp[0] == 0x31 then
			ui.tree_set_alt_value(HOLDER, "Employee")
		else
			ui.tree_set_alt_value(HOLDER, "Unknown")	
		end
		ui.tree_set_value(HOLDER, resp)
	end

	sw,resp = raw_get_data(0x04)
	if sw==0x9000 then
     		INE = ui.tree_add_node(APP,"ine","INE", "04", #resp)
		ui_parse_cstring(INE,resp)
	end

	sw,resp = raw_get_data(0x05)
	if sw==0x9000 then
     		VS = ui.tree_add_node(APP,"validity start","VALIDITY_START", "05", #resp)
		ui_parse_cstring(VS,resp)
	end

	sw,resp = raw_get_data(0x06)
	if sw==0x9000 then
     		VE = ui.tree_add_node(APP,"validity end","VALIDITY_END", "06", #resp)
		ui_parse_cstring(VE,resp)
	end
	--sw,resp = raw_get_data(0x21)
	--if sw==0x9000 then
     	--	TEST = ui.tree_add_node(APP,"test","TEST", "21", #resp)
	--	ui_parse_cstring(TEST,resp)
	--end
  end

  card.disconnect()

else
  ui.question("No card detected",{"OK"})
end

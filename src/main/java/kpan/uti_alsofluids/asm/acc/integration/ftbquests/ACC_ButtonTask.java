package kpan.uti_alsofluids.asm.acc.integration.ftbquests;


import kpan.uti_alsofluids.asm.core.adapters.MixinAccessorAdapter.NewField;

public interface ACC_ButtonTask {

	//新しいインスタンスフィールドの追加&getter作成
	//getterとsetterの両方を作成する必要はないが、初期化する方法が無いので両方作るのが基本
	//@NewFieldはgetterとsetterの両方に必要
	@NewField
	String get_localizedLine();

	//新しいインスタンスフィールドの追加&setter作成
	//説明はgetter同様
	@NewField
	void set_localizedLine(String value);

}

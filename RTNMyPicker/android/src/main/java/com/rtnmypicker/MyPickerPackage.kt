package com.rtnmypicker;

import com.facebook.react.TurboReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfoProvider
import com.facebook.react.module.model.ReactModuleInfo
import java.util.HashMap;

class MyPickerPackage : TurboReactPackage() {
    override fun getModule(name: String, reactContext: ReactApplicationContext): NativeModule? {
        return if (name == MyPickerModule.NAME) {
            MyPickerModule(reactContext)
        } else {
            null
        }
    }

    override fun getReactModuleInfoProvider(): ReactModuleInfoProvider? {
        return ReactModuleInfoProvider {
            val moduleInfos: MutableMap<String, ReactModuleInfo> = HashMap()
            moduleInfos[MyPickerModule.NAME] = ReactModuleInfo(
                MyPickerModule.NAME,
                MyPickerModule.NAME,
                false, // canOverrideExistingModule
                false, // needsEagerInit
                false, // isCxxModule
                true // isTurboModule
            )
            moduleInfos
        }
    }
}
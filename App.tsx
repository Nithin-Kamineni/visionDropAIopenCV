/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import React from 'react';
import type {PropsWithChildren} from 'react';
import {ScrollView, StyleSheet, View} from 'react-native';

import {
  PermissionsAndroid,
  SafeAreaView,
  StatusBar,
  TouchableOpacity,
  Text,
  Image,
  useColorScheme,
} from 'react-native';

import {
  Colors,
  DebugInstructions,
  Header,
  LearnMoreLinks,
  ReloadInstructions,
} from 'react-native/Libraries/NewAppScreen';

import RTNMyPicker from 'rtn-my-picker/js/NativeMyPicker';

type SectionProps = PropsWithChildren<{
  title: string;
}>;

function App(): React.JSX.Element {
  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  const [imageUri, setImageUri] = React.useState(
    'https://letsenhance.io/static/8f5e523ee6b2479e26ecc91b9c25261e/1015f/MainAfter.jpg',
  );

  const requestPermissionAsync = async () => {
    const result = await PermissionsAndroid.requestMultiple([
      PermissionsAndroid.PERMISSIONS.READ_MEDIA_IMAGES,
      PermissionsAndroid.PERMISSIONS.READ_EXTERNAL_STORAGE,
    ]);
    console.log('result1', result);
    if (
      result['android.permission.READ_MEDIA_IMAGES'] === 'granted' ||
      result['android.permission.READ_EXTERNAL_STORAGE'] === 'granted'
    ) {
      const result = await RTNMyPicker?.pickImage();
      console.log('result2', result?.slice(0, 30));
      if (result) {
        setImageUri(result);
      }
    }
  };

  return (
    <SafeAreaView style={backgroundStyle}>
      <StatusBar
        barStyle={isDarkMode ? 'light-content' : 'dark-content'}
        backgroundColor={backgroundStyle.backgroundColor}
      />
      <ScrollView
        contentInsetAdjustmentBehavior="automatic"
        style={backgroundStyle}>
        {/* <Header /> */}
        <View
          style={{
            backgroundColor: isDarkMode ? Colors.black : Colors.white,
          }}>
          <Text>oiii</Text>
        </View>
        <TouchableOpacity onPress={() => requestPermissionAsync()}>
          <Text style={{fontSize: 30, color: 'red'}}>Capture Photo</Text>
        </TouchableOpacity>
        {imageUri !== '' && (
          <Image source={{uri: imageUri}} style={{width: 300, height: 300}} />
        )}
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
  },
  highlight: {
    fontWeight: '700',
  },
});

export default App;

// yarn add ./RTNMyPicker

// node VisionDropOpenCV/node_modules/react-native/scripts/generate-codegen-artifacts.js --path VisionDropOpenCV/ --outputPath VisionDropOpenCV/RTNMyPicker/generated/ --targetPlatform all

// ./gradlew generateCodegenArtifactsFromSchema   |    gradle generateCodegenArtifactsFromSchema

// yarn react-native run-android --active-arch-only

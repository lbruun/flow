/**
 * NOTICE: this is an auto-generated file
 *
 * This file has been generated by the `flow:prepare-frontend` maven goal.
 * This file will be overwritten on every run. Any custom changes should be made to webpack.config.js
 */
const fs = require('fs');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const ScriptExtHtmlWebpackPlugin = require('script-ext-html-webpack-plugin');
const CompressionPlugin = require('compression-webpack-plugin');

// Flow plugins
const StatsPlugin = require('@vaadin/stats-plugin');
const ApplicationThemePlugin = require('@vaadin/application-theme-plugin');

const path = require('path');

// this matches /theme/my-theme/ and is used to check css url handling and file path build.
const themePartRegex = /(\\|\/)theme\1[\s\S]*?\1/;

// the folder of app resources:
//  - flow templates for classic Flow
//  - client code with index.html and index.[ts/js] for CCDM
const frontendFolder = '[to-be-generated-by-flow]';
const fileNameOfTheFlowGeneratedMainEntryPoint = '[to-be-generated-by-flow]';
const mavenOutputFolderForFlowBundledFiles = '[to-be-generated-by-flow]';
const useClientSideIndexFileForBootstrapping = '[to-be-generated-by-flow]';
const clientSideIndexHTML = '[to-be-generated-by-flow]';
const clientSideIndexEntryPoint = '[to-be-generated-by-flow]';
const devmodeGizmoJS = '[to-be-generated-by-flow]';
// public path for resources, must match Flow VAADIN_BUILD
const build = 'build';
// public path for resources, must match the request used in flow to get the /build/stats.json file
const config = 'config';
// folder for outputting vaadin-bundle and other fragments
const buildFolder = `${mavenOutputFolderForFlowBundledFiles}/${build}`;
// folder for outputting stats.json
const confFolder = `${mavenOutputFolderForFlowBundledFiles}/${config}`;
// file which is used by flow to read templates for server `@Id` binding
const statsFile = `${confFolder}/stats.json`;

// Folders in the project which can contain static assets.
const projectStaticAssetsFolders = [
  path.resolve(__dirname, 'src', 'main', 'resources', 'META-INF', 'resources'),
  path.resolve(__dirname, 'src', 'main', 'resources', 'static'),
  frontendFolder
];

const projectStaticAssetsOutputFolder = [to-be-generated-by-flow];

// Folders in the project which can contain application themes
const themeProjectFolders = projectStaticAssetsFolders.map((folder) =>
  path.resolve(folder, 'theme')
);


// Target flow-fronted auto generated to be the actual target folder
const flowFrontendFolder = '[to-be-generated-by-flow]';

// make sure that build folder exists before outputting anything
const mkdirp = require('mkdirp');

const devMode = process.argv.find(v => v.indexOf('webpack-dev-server') >= 0);

!devMode && mkdirp(buildFolder);
mkdirp(confFolder);

let stats;

// Open a connection with the Java dev-mode handler in order to finish
// webpack-dev-mode when it exits or crashes.
const watchDogPrefix = '--watchDogPort=';
let watchDogPort = devMode && process.argv.find(v => v.indexOf(watchDogPrefix) >= 0);
if (watchDogPort) {
  watchDogPort = watchDogPort.substr(watchDogPrefix.length);
  const runWatchDog = () => {
    const client = new require('net').Socket();
    client.setEncoding('utf8');
    client.on('error', function () {
      console.log("Watchdog connection error. Terminating webpack process...");
      client.destroy();
      process.exit(0);
    });
    client.on('close', function () {
      client.destroy();
      runWatchDog();
    });

    client.connect(watchDogPort, 'localhost');
  }
  runWatchDog();
}

// Compute the entries that webpack have to visit
const webPackEntries = {};
if (useClientSideIndexFileForBootstrapping) {
  webPackEntries.bundle = clientSideIndexEntryPoint;
  const dirName = path.dirname(fileNameOfTheFlowGeneratedMainEntryPoint);
  const baseName = path.basename(fileNameOfTheFlowGeneratedMainEntryPoint, '.js');
  if (fs.readdirSync(dirName).filter(fileName => !fileName.startsWith(baseName)).length) {
    // if there are vaadin exported views, add a second entry
    webPackEntries.export = fileNameOfTheFlowGeneratedMainEntryPoint;
  }
} else {
  webPackEntries.bundle = fileNameOfTheFlowGeneratedMainEntryPoint;
}

if (devMode) {
  webPackEntries.devmodeGizmo = devmodeGizmoJS;
}

exports = {
  frontendFolder: `${frontendFolder}`,
  buildFolder: `${buildFolder}`,
  confFolder: `${confFolder}`
};

module.exports = {
  mode: 'production',
  context: frontendFolder,
  entry: webPackEntries,

  output: {
    filename: `${build}/vaadin-[name]-[contenthash].cache.js`,
    path: mavenOutputFolderForFlowBundledFiles,
    publicPath: 'VAADIN/',
  },

  resolve: {
    // Search for import 'x/y' inside these folders, used at least for importing an application theme
    modules: [
      'node_modules',
      flowFrontendFolder,
      ...projectStaticAssetsFolders,
    ],
    extensions: ['.ts', '.js'],
    alias: {
      Frontend: frontendFolder
    }
  },

  devServer: {
    // webpack-dev-server serves ./ ,  webpack-generated,  and java webapp
    contentBase: [mavenOutputFolderForFlowBundledFiles, 'src/main/webapp'],
    after: function(app, server) {
      app.get(`/stats.json`, function(req, res) {
        res.json(stats);
      });
      app.get(`/stats.hash`, function(req, res) {
        res.json(stats.hash.toString());
      });
      app.get(`/assetsByChunkName`, function(req, res) {
        res.json(stats.assetsByChunkName);
      });
      app.get(`/stop`, function(req, res) {
        // eslint-disable-next-line no-console
        console.log("Stopped 'webpack-dev-server'");
        process.exit(0);
      });
    }
  },

  module: {
    rules: [
      {
        test: /\.ts$/,
        use: [
          'awesome-typescript-loader'
        ]
      },
      {
        test: /\.css$/i,
        use: [
          {
            loader: "lit-css-loader"
          },
          {
            loader: "extract-loader"
          },
          {
            loader: 'css-loader',
            options: {
              url: (url, resourcePath) => {
                // Only translate files from node_modules
                const resolve = resourcePath.match(/(\\|\/)node_modules\1/);
                const themeResource = resourcePath.match(themePartRegex) && url.match(/^theme\/[\s\S]*?\//);
                return resolve || themeResource;
              },
              // use theme-loader to also handle any imports in css files
              importLoaders: 1
            },
          },
          {
            // theme-loader will change any url starting with './' to start with 'VAADIN/static' instead
            // NOTE! this loader should be here so it's run before css-loader as loaders are applied Right-To-Left
            loader: '@vaadin/theme-loader',
            options: {
              devMode: devMode
            }
          }
        ],
      },
      {
        // File-loader only copies files used as imports in .js files or handled by css-loader
        test: /\.(png|gif|jpg|jpeg|svg|eot|woff|woff2|ttf)$/,
        use: [{
          loader: 'file-loader',
          options: {
            outputPath: 'static/',
            name(resourcePath, resourceQuery) {
              const urlResource = resourcePath.substring(frontendFolder.length);
              if(urlResource.match(themePartRegex)){
                return /^(\\|\/)theme\1[\s\S]*?\1(.*)/.exec(urlResource)[2].replace(/\\/, "/");
              }
              return '[path][name].[ext]';
            }
          }
        }],
      },
    ]
  },
  performance: {
    maxEntrypointSize: 2097152, // 2MB
    maxAssetSize: 2097152 // 2MB
  },
  plugins: [
    // Generate compressed bundles when not devMode
    !devMode && new CompressionPlugin(),

    new ApplicationThemePlugin({
      themeResourceFolder: path.resolve(flowFrontendFolder, 'theme'),
      themeProjectFolders: themeProjectFolders,
      projectStaticAssetsOutputFolder: projectStaticAssetsOutputFolder,
    }),

    new StatsPlugin({
      devMode: devMode,
      statsFile: statsFile,
      setResults: function (statsFile) {
        stats = statsFile;
      }
    }),

    // Includes JS output bundles into "index.html"
    useClientSideIndexFileForBootstrapping && new HtmlWebpackPlugin({
      template: clientSideIndexHTML,
      inject: 'head',
      chunks: ['bundle', ...(devMode ? ['devmodeGizmo'] : [])]
    }),
    useClientSideIndexFileForBootstrapping && new ScriptExtHtmlWebpackPlugin({
      defaultAttribute: 'defer'
    }),
  ].filter(Boolean)
};

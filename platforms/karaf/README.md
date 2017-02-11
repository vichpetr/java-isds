# Installation on ApacheKaraf

## Karaf 2.x

Enter following commands to karaf shell
```shell
# instruct karaf to download features.xml from it's known maven repositories
# replace version for current one
features:addurl mvn:cz.abclinuxu.datoveschranky/karaf/1.0.0-SNAPSHOT/xml/features

# install feature
features:install isds
```

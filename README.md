## Bouncer

Decompilation/deobfuscation of 2001 javame game Bounce

![alt text](https://github.com/misterjdrg/Bouncer/blob/main/images/lvl1.png?raw=true)

Level 1

![alt text](https://github.com/misterjdrg/Bouncer/blob/main/images/lvl4.png?raw=true)

Level 4



### Resource (ott, png, localisation, levels) files not included

Original jar hashes

|  hash  | value                                                            |
|--------|------------------------------------------------------------------|
|  md5   | 35940a0f3f7d97648f416bf36a76e166                                 |
|  sha1  | 5890b57d1a3300830a0dae7cad0363fd5aa97a11                         |
| sha256 | 77a24bf1de10ad8a1c440fc96aa1ed2e4b29f2102771f268eaa93ff910b55ee8 |

### Build

#### 1. copy resources into `src/main/resources/`
 - sounds/pickup.ott
 - sounds/pop.ott
 - sounds/up.ott
 - levels/J2MElvl.001
 - levels/J2MElvl.002
 - levels/J2MElvl.003
 - levels/J2MElvl.004
 - levels/J2MElvl.005
 - levels/J2MElvl.006
 - levels/J2MElvl.007
 - levels/J2MElvl.008
 - levels/J2MElvl.009
 - levels/J2MElvl.010
 - levels/J2MElvl.011
 - icons/bouncesplash.png
 - icons/icon.png
 - icons/nokiagames.png
 - icons/objects_nm.png
 - lang.zh-TW
 - lang.zh-CN
 - lang.xx
 - lang.th-TH

#### 2. run
```bash
    mvn package
```

#### 3. Done

Output in `target/bounce-latest.jar`

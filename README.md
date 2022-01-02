# MagStarMineArea

[![Version](https://img.shields.io/badge/dynamic/json?label=Version&query=%24.tag_name&url=https%3A%2F%2Fapi.github.com%2Frepos%2FBerryso%2FMagStarMineArea%2Freleases%2Flatest)](https://github.com/Berryso/MagStarMineArea/releases)

****
**A 100% customizable plugin to edit your Mine Area.**  
**这是一款100%自定义度的插件，用于改变你的矿区。**  
**Now, enjoy your journey in this advanced plugin.**  
**现在，与我一起，在这款高端的插件中遨游吧。**
****
## About This Plugin: //关于这款插件
> #### Development Environment: //开发环境
>
*Minecraft 1.18*  
*Java 17*
> #### Dependencies: //开发依赖
>
*Spigot API-1.18 R0.1-SNAPSHOT*
> #### Author and Team: //作者及代表团队
>
*Developed by Berry_so //由Berry_so开发*  
*Represents the Stars Creation //代表Stars Creation星星逹制作组*
****
## API Usage: //API用法

```java
public class demo {
    public List<ItemStack> list(Block block) {
        return getDropList(block);
    }

    @EventHandler
    public void e(PlayerMineEvent e){
        e.setCancelled(true);
    }
}
```
****
## Update Link: //上传
### [GitHub](https://github.com/Berryso/MagStarMineArea)
### [MCBBS](https://www.mcbbs.net/thread-1288046-1-1.html)
****
## About the Structure: //有关结构
> #### Package: //包
>
### `xyz.magstar.minearea`
### `xyz.magstar.minearea.api`
### `xyz.magstar.minearea.apimanager`
### `xyz.magstar.minearea.events`
### `xyz.magstar.minearea.handlers`
### `xyz.magstar.minearea.utils`
>#### Classes: //类
>
### `Main`
### `PlayerMineEvent`
### `APIDigManager`
### `CommandHandler, EventHandlers, TabHandler`
### `FileUtils, GeneralUtils, GuiUtils`
****
## Version History: //版本历史记录
> #### Version 1.0.2, GREATLY adjusted the structure of this plugin, to make it more easy to read. Added PlayerMineEvent to API.
> #### 版本1.0.2，大幅修改了插件的结构，使源码更方便阅读。向API中添加了PlayerMineEvent事件。
> #### Version 1.0.1, fixed logical errors in the methods when add player Exp;
> #### 版本1.0.1，修复了在给予玩家经验时的逻辑错误；
> #### Version 1.0.0, published in 2021.12.29;
> #### 版本1.0.0，在2021年12月29日发布；
****
## Contact us: //联系我们
> #### For Chinese Users: //对于中国用户
>
### `QQ: 2057266133`
> #### For Foreign Users: //对于国外用户
>
### `Discord: Berry_so #5251`

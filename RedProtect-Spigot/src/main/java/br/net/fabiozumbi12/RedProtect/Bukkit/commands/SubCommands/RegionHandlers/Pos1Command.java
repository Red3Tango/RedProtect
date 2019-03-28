/*
 *
 * Copyright (c) 2019 - @FabioZumbi12
 * Last Modified: 28/03/19 20:18
 *
 * This class is provided 'as-is', without any express or implied warranty. In no event will the authors be held liable for any
 *  damages arising from the use of this class.
 *
 * Permission is granted to anyone to use this class for any purpose, including commercial plugins, and to alter it and
 * redistribute it freely, subject to the following restrictions:
 * 1 - The origin of this class must not be misrepresented; you must not claim that you wrote the original software. If you
 * use this class in other plugins, an acknowledgment in the plugin documentation would be appreciated but is not required.
 * 2 - Altered source versions must be plainly marked as such, and must not be misrepresented as being the original class.
 * 3 - This notice may not be removed or altered from any source distribution.
 *
 * Esta classe é fornecida "como está", sem qualquer garantia expressa ou implícita. Em nenhum caso os autores serão
 * responsabilizados por quaisquer danos decorrentes do uso desta classe.
 *
 * É concedida permissão a qualquer pessoa para usar esta classe para qualquer finalidade, incluindo plugins pagos, e para
 * alterá-lo e redistribuí-lo livremente, sujeito às seguintes restrições:
 * 1 - A origem desta classe não deve ser deturpada; você não deve afirmar que escreveu a classe original. Se você usar esta
 *  classe em um plugin, uma confirmação de autoria na documentação do plugin será apreciada, mas não é necessária.
 * 2 - Versões de origem alteradas devem ser claramente marcadas como tal e não devem ser deturpadas como sendo a
 * classe original.
 * 3 - Este aviso não pode ser removido ou alterado de qualquer distribuição de origem.
 *
 */

package br.net.fabiozumbi12.RedProtect.Bukkit.commands.SubCommands.RegionHandlers;

import br.net.fabiozumbi12.RedProtect.Bukkit.helpers.RPUtil;
import br.net.fabiozumbi12.RedProtect.Bukkit.RedProtect;
import br.net.fabiozumbi12.RedProtect.Bukkit.commands.SubCommand;
import br.net.fabiozumbi12.RedProtect.Bukkit.config.RPConfig;
import br.net.fabiozumbi12.RedProtect.Bukkit.config.RPLang;
import br.net.fabiozumbi12.RedProtect.Bukkit.hooks.WEListener;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static br.net.fabiozumbi12.RedProtect.Bukkit.helpers.RPUtil.HandleHelpPage;

public class Pos1Command implements SubCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            HandleHelpPage(sender, 1);
            return true;
        }

        Player player = (Player) sender;

        String claimmode = RPConfig.getWorldClaimType(player.getWorld().getName());
        if (!claimmode.equalsIgnoreCase("WAND") && !claimmode.equalsIgnoreCase("BOTH") && !RedProtect.get().ph.hasCommandPerm(player, "redefine")) {
            return true;
        }

        if (args.length == 0) {
            Location pl = player.getLocation();
            RedProtect.get().firstLocationSelections.put(player, pl);
            player.sendMessage(RPLang.get("playerlistener.wand1") + RPLang.get("general.color") + " (" + ChatColor.GOLD + pl.getBlockX() + RPLang.get("general.color") + ", " + ChatColor.GOLD + pl.getBlockY() + RPLang.get("general.color") + ", " + ChatColor.GOLD + pl.getBlockZ() + RPLang.get("general.color") + ").");

            //show preview border
            if (RedProtect.get().firstLocationSelections.containsKey(player) && RedProtect.get().secondLocationSelections.containsKey(player)) {
                Location loc1 = RedProtect.get().firstLocationSelections.get(player);
                Location loc2 = RedProtect.get().secondLocationSelections.get(player);
                if (RedProtect.get().WE && RPConfig.getBool("hooks.useWECUI")) {
                    WEListener.setSelectionRP(player, loc1, loc2);
                }

                if (loc1.getWorld().equals(loc2.getWorld()) && loc1.distanceSquared(loc2) > RPConfig.getInt("region-settings.define-max-distance")) {
                    Double dist = loc1.distanceSquared(loc2);
                    RPLang.sendMessage(player, String.format(RPLang.get("regionbuilder.selection.maxdefine"), RPConfig.getInt("region-settings.define-max-distance"), dist.intValue()));
                } else {
                    RPUtil.addBorder(player, RPUtil.get4Points(loc1, loc2, player.getLocation().getBlockY()));
                }
            }
            return true;
        }

        RPLang.sendCommandHelp(sender, "pos1", true);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}

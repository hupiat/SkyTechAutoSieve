package com.skytechautosieve.sieves;

import com.skytechautosieve.NetworkHandler;
import com.skytechautosieve.sieves.networking.PacketOpenHUD;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandOpenSieveHUD extends CommandBase {

	@Override
	public String getName() {
		return "open_sieve_hud";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/open_sieve_hud";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (!(sender instanceof EntityPlayerMP)) {
			throw new CommandException("This command must be executed by a player.");
		}
		EntityPlayerMP player = (EntityPlayerMP) sender;

		NetworkHandler.NETWORK.sendTo(new PacketOpenHUD(), player);
	}
}

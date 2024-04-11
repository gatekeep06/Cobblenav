package com.metacontent.cobblenav.networking;

import com.metacontent.cobblenav.Cobblenav;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class CobblenavPackets {
    public static final Identifier SPAWN_MAP_PACKET_SERVER = new Identifier(Cobblenav.ID, "spawn_map_packet_server");
    public static final Identifier SPAWN_MAP_PACKET_CLIENT = new Identifier(Cobblenav.ID, "spawn_map_packet_client");
    public static final Identifier CONTACT_DATA_PACKET_SERVER = new Identifier(Cobblenav.ID, "contact_data_packet_server");
    public static final Identifier CONTACT_DATA_PACKET_CLIENT = new Identifier(Cobblenav.ID, "contact_data_packet_client");
    public static final Identifier BEST_POKEMON_PACKET_SERVER = new Identifier(Cobblenav.ID, "best_pokemon_packet_server");
    public static final Identifier BEST_POKEMON_PACKET_CLIENT = new Identifier(Cobblenav.ID, "best_pokemon_packet_client");
    public static final Identifier SAVE_FOUND_POKEMON_PACKET = new Identifier(Cobblenav.ID, "save_found_pokemon_packet");
    public static final Identifier RENDERABLE_POKEMON_PACKET_CLIENT = new Identifier(Cobblenav.ID, "renderable_pokemon_packet_client");
    public static final Identifier RENDERABLE_POKEMON_PACKET_SERVER = new Identifier(Cobblenav.ID, "renderable_pokemon_packet_server");
    public static final Identifier REMOVE_LAST_FOUND_POKEMON_PACKET = new Identifier(Cobblenav.ID, "remove_last_found_pokemon_packet");
    public static final Identifier DELETE_CONTACT_PACKET = new Identifier(Cobblenav.ID, "delete_contact_packet");
    public static final Identifier TRACKED_ENTITY_ID_PACKET_SERVER = new Identifier(Cobblenav.ID, "tracked_entity_id_packet_server");
    public static final Identifier TRACKED_ENTITY_ID_PACKET_CLIENT = new Identifier(Cobblenav.ID, "tracked_entity_id_packet_client");
    public static final Identifier SAVE_PREFERENCES_PACKET = new Identifier(Cobblenav.ID, "save_preferences_packet");
    public static final Identifier SAVED_PREFERENCES_PACKET_CLIENT = new Identifier(Cobblenav.ID, "saved_preferences_packet_client");
    public static final Identifier SAVED_PREFERENCES_PACKET_SERVER = new Identifier(Cobblenav.ID, "saved_preferences_packet_server");


    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(SPAWN_MAP_PACKET_SERVER, SpawnMapPacketServerReceiver::receive);
        ServerPlayNetworking.registerGlobalReceiver(CONTACT_DATA_PACKET_SERVER, ContactDataPacketServerReceiver::receive);
        ServerPlayNetworking.registerGlobalReceiver(BEST_POKEMON_PACKET_SERVER, BestPokemonPacketServerReceiver::receive);
        ServerPlayNetworking.registerGlobalReceiver(SAVE_FOUND_POKEMON_PACKET, SaveFoundPokemonPacketReceiver::receive);
        ServerPlayNetworking.registerGlobalReceiver(RENDERABLE_POKEMON_PACKET_SERVER, RenderablePokemonPacketServerReceiver::receive);
        ServerPlayNetworking.registerGlobalReceiver(REMOVE_LAST_FOUND_POKEMON_PACKET, RemoveLastFoundPokemonPacketReceiver::receive);
        ServerPlayNetworking.registerGlobalReceiver(DELETE_CONTACT_PACKET, DeleteContactPacketReceiver::receive);
        ServerPlayNetworking.registerGlobalReceiver(TRACKED_ENTITY_ID_PACKET_SERVER, TrackedEntityIdPacketServerReceiver::receive);
        ServerPlayNetworking.registerGlobalReceiver(SAVED_PREFERENCES_PACKET_SERVER, SavedPreferencesPacketServerReceiver::receive);
        ServerPlayNetworking.registerGlobalReceiver(SAVE_PREFERENCES_PACKET, SavePreferencesPacketReceiver::receive);
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(SPAWN_MAP_PACKET_CLIENT, SpawnMapPacketClientReceiver::receive);
        ClientPlayNetworking.registerGlobalReceiver(CONTACT_DATA_PACKET_CLIENT, ContactDataPacketClientReceiver::receive);
        ClientPlayNetworking.registerGlobalReceiver(BEST_POKEMON_PACKET_CLIENT, BestPokemonPacketClientReceiver::receive);
        ClientPlayNetworking.registerGlobalReceiver(RENDERABLE_POKEMON_PACKET_CLIENT, RenderablePokemonPacketClientReceiver::receive);
        ClientPlayNetworking.registerGlobalReceiver(TRACKED_ENTITY_ID_PACKET_CLIENT, TrackedEntityIdPacketClientReceiver::receive);
        ClientPlayNetworking.registerGlobalReceiver(SAVED_PREFERENCES_PACKET_CLIENT, SavedPreferencesPacketClientReceiver::receive);
    }
}

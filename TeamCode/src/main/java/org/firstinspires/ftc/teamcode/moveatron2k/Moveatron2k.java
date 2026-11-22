    /** Which frame the manual drive axes are expressed in. */
    public enum MoveType {
        /** Axes are the bot's body frame: +forward = the nose, +strafe = left. */
        BotCentric,
        /** Axes are the Moveatron2k frame (world): +x = wherever the bot faced
         *  at init (or after {@link #setPosition}), +y = that direction's left.
         *  Rotated into the body frame against the current heading each call. */
        FieldCentric
    }

     * Manual drive path: applies the given powers straight to the drivetrain —
     * no point-to-point controller, no target set, {@link #tick()} still no-ops.
     * The axes are interpreted in the frame the {@link MoveType} names; for
     * {@link MoveType#FieldCentric} the world-frame powers are rotated into the
     * body frame against the bot's CURRENT heading (fresh Pinpoint read) each
     * call, so pushing +forward always drives toward the frame's x-axis no
     * matter which way the bot has since rotated.
     *
     * Does NOTHING while the bot is busy with a {@link #goTo} move (i.e.
     * {@link #isBusy()} is true): the closed-loop move owns the wheels until it
     * finishes inside tolerance (its own tick) or {@link #cancel()} clears it.
     *
     * @param moveType which frame the powers are expressed in
     * @param powers   +forward = nose, +strafe = left, turn: + = CCW as seen
     *                 from above (physical convention — the closed loop's
     *                 {@link Config#reverseRotation} compensation is goTo's
     *                 business, not the driver's)
     */
    public void manual(MoveType moveType, DrivePowers powers) {
        if (busy) {
            return;
        }

        switch (moveType) {
            case FieldCentric:
                drivetrain.applyDrive(FieldCentricDrive.drive(
                        powers.forward(), powers.strafe(), powers.turn(),
                        Math.toRadians(currentPose().headingDeg)));
                break;
            case BotCentric:
            default:
                drivetrain.applyDrive(BotCentricDrive.drive(
                        powers.forward(), powers.strafe(), powers.turn()));
                break;
        }
    }

    /**